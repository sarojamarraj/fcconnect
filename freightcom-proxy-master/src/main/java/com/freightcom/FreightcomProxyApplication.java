package com.freightcom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableZuulProxy
public class FreightcomProxyApplication extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(FreightcomProxyApplication.class);
    }

    public static void main(String[] args)
    {
        SpringApplication.run(FreightcomProxyApplication.class, args);
    }

    @Bean
    public StaticFilter simpleFilter() {
      return new StaticFilter();
    }

}
