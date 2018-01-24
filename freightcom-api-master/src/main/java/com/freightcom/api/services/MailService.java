package com.freightcom.api.services;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.freightcom.api.model.SentEmail;
import com.freightcom.api.repositories.ObjectBase;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final SendGrid sendGrid;
    private final TemplateEngine emailTemplateEngine;
    private final ObjectBase objectBase;

    @Value("${freightcom.email.default.from}")
    private String defaultFrom;

    @Autowired
    public MailService(final SendGrid sendGrid, final TemplateEngine emailTemplateEngine, final ObjectBase objectBase)
    {
        this.sendGrid = sendGrid;
        this.emailTemplateEngine = emailTemplateEngine;
        this.objectBase = objectBase;

        log.debug("MAIL SENDER INITIALIZED " + sendGrid);
    }

    public void send(final String subject, String email, String templateName, Map<String, Object> values)
            throws Exception
    {
        send(subject, email, templateName, values, defaultFrom);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void send(final String subject, final String email, final String templateName,
            final Map<String, Object> values, final String fromAddress) throws Exception
    {
        Email from = new Email(fromAddress);
        Email to = new Email(email);

        log.debug("MAIL SENDER FROM " + fromAddress);
        log.debug("MAIL SENDER TO " + email);

        final Context context = new Context();

        for (String key : values.keySet()) {
            context.setVariable(key, values.get(key));
        }

        String htmlContent = null;

        try {
            htmlContent = emailTemplateEngine.process(templateName, context);
        } catch (Exception e) {
            log.debug("MAIL SENDER Failed template located " + templateName + " " + e.getMessage());

        }

        if (htmlContent == null) {
            throw new Exception("MAIL SENDER MISSING CONTENT");
        }

        Content content = new Content("text/html", htmlContent);

        SentEmail emailRecord = new SentEmail(fromAddress, email, subject, content.getValue());
        log.debug("MAIL SENDER SAVING EMAIL RECORD " + emailRecord);
        objectBase.save(emailRecord);


        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        boolean ok = false;

        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();

            // TODO: log emails sent and failed

            for (int attempt = 0; attempt < 10; attempt++) {
                try {
                    Response response = sendGrid.api(request);

                    if (response.statusCode <= 300) {
                        ok = true;
                        log.debug("EMAIL SET TO " + email);
                        break;
                    }
                } catch (IOException e) {
                    Thread.sleep(1000L);
                    log.error("ERROR IN SENDING MESSAGE " + e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            log.error("ERROR IN SENDING MESSAGE " + e.getMessage(), e);
            throw e;
        }

        if (!ok) {
            throw new Exception("Unable to send email");
        }
    }
}
