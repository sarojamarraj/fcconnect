package com.freightcom.api.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan
@EnableJpaRepositories
@EnableAutoConfiguration
public class Client
{

    public static void main(String[] args)
    {
        System.out.println("hello world");
        SpringApplication.run(Client.class, args);

    }

}
