package com.freightcom.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.MailService;
import com.sendgrid.SendGrid;

@Configuration
@ConfigurationProperties(prefix = "email")
public class MailConfiguration
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Value("${email.config.host}")
    private String host;

    @Value("${email.config.port}")
    private String port;

    @Value("${email.config.protocol}")
    private String protocol;

    @Value("${email.config.username}")
    private String username;

    @Value("${email.config.password}")
    private String password;

    @Bean
    public JavaMailSender mailSender() throws IOException {

        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        log.debug("MAIL CONFIG " + host + " " + port + " " + protocol + " " + username);

        // Basic mail sender configuration
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setProtocol(protocol);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        log.debug("MAIL CONFIGURED RETURNING SENDER");

        return mailSender;

    }

    @Bean
    public SimpleMailMessage templateMessage()
    {
        return new SimpleMailMessage();
    }

    @Bean
    public MailService getMailService(final SendGrid sendGrid, final TemplateEngine emailTemplateEngine, final ObjectBase objectBase)
    {
        return new MailService(sendGrid, emailTemplateEngine, objectBase);
    }
}
