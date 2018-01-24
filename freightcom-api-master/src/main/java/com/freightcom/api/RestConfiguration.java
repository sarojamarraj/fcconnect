package com.freightcom.api;

import org.apache.catalina.filters.RequestDumperFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.freightcom.api.util.CsvMessageConverter;

@Configuration
public class RestConfiguration
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // you USUALLY want this
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public CsvMessageConverter csvMessageConverter()
    {
        return new CsvMessageConverter();
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter()
    {
        return new ByteArrayHttpMessageConverter();
    }

    @Bean
    public FilterRegistrationBean requestDumperFilter()
    {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new RequestDumperFilter());
        registration.addUrlPatterns("/*");

        log.debug("FILTER REGISTRATION");

        return registration;
    }
}
