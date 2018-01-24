package com.freightcom.api;

import java.util.Collections;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.sendgrid.SendGrid;

@Component
@ConfigurationProperties(prefix = "freightcom")
public class Beans
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${freightcom.sendgrid.api_key}")
    private String sendgrid_api_key;

    @Value("${freightcom.slack.url}")
    private String slackUrl;

    @Bean
    public SendGrid sendGrid()
    {
        log.debug("SENDGRID API KEY " + sendgrid_api_key);
        return new SendGrid(sendgrid_api_key);
    }

    @Bean
    public TemplateEngine emailTemplateEngine()
    {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for TEXT emails
        templateEngine.addTemplateResolver(textTemplateResolver());
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());

        return templateEngine;
    }

    private ITemplateResolver textTemplateResolver()
    {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
        templateResolver.setPrefix("classpath:/mail/");
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode("text");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private ITemplateResolver htmlTemplateResolver()
    {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        return templateResolver;
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor()
    {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(100);
        executor.initialize();
        return executor;
    }

    
    @Bean(name = "slackService")
    public SlackService getSlackService()
    {
        return new SlackService(slackUrl);
    }
}
