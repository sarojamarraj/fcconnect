package com.freightcom.api;

import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.bugsnag.Bugsnag;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.ObjectBaseImpl;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@ConfigurationProperties(prefix = "freightcom")
public class Application extends SpringBootServletInitializer
{
    @Value("${freightcom.bugsnag_api_key}")
    private String bugsnag_api_key;

    @Value("${freightcom.bugsnag_devel}")
    private boolean bugsnag_devel;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**");
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder)
    {
        return builder.build();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(Application.class);
    }

    public static void main(String[] args)
    {
        TimeZone.setDefault(TimeZone.getTimeZone("Etc/UTC"));
        ApplicationContext context = SpringApplication.run(Application.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) context.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }

    @Bean
    public ObjectBase objectBase()
    {
        return new ObjectBaseImpl();
    }

    @Bean
    public AlwaysSendUnauthorized401AuthenticationEntryPoint alwaysSendUnauthorized401AuthenticationEntryPoint()
    {
        return new AlwaysSendUnauthorized401AuthenticationEntryPoint();
    }

    @Bean
    public SuccessfulAuthentication successfulAuthentication()
    {
        return new SuccessfulAuthentication();
    }

    @Bean
    public FailedAuthentication failedAuthentication()
    {
        return new FailedAuthentication();
    }

    @Bean
    public HttpSessionListener httpSessionListener(final ApplicationEventPublisher publisher)
    {
        // MySessionListener should implement
        // javax.servlet.http.HttpSessionListener
        return new SessionListener(publisher);
    }

    @Bean
    public ErrorAttributes errorAttributes()
    {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes,
                    boolean includeStackTrace)
            {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                // Customize the default entries in errorAttributes to suit your
                // needs
                log.debug("CUSTOM ERROR ATTRIBUTES " + requestAttributes + " \nFOO\n" + errorAttributes);
                return errorAttributes;
            }

        };
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder()
    {
        log.debug("created password encoder passwordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Scope("prototype")
    public Bugsnag bugsnag()
    {
        if (bugsnag_devel) {
            return null;
        } else {
            Bugsnag bugsnag =  new Bugsnag(bugsnag_api_key);

            bugsnag.addCallback(new BugsnagCallback());

            return bugsnag;
        }
    }

    private static final String SPRING_HATEOAS_OBJECT_MAPPER = "_halObjectMapper";

    @Autowired
    @Qualifier(SPRING_HATEOAS_OBJECT_MAPPER)
    private ObjectMapper springHateoasObjectMapper;

    @Bean(name ="foobar")
    public ObjectMapper objectMapper()
    {
        log.debug("RUNNING HAL OBJECT MAPPER");

        springHateoasObjectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        springHateoasObjectMapper.registerModules(new JavaTimeModule());
        springHateoasObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return springHateoasObjectMapper;
    }

    @Autowired
    @Qualifier("foobar")
    private ObjectMapper foobarObjectMapper;

}
