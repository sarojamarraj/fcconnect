package com.freightcom.api;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

public class Messages
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

    @PostConstruct
    private void init()
    {
        accessor = new MessageSourceAccessor(messageSource, Locale.ENGLISH);
    }

    public String get(String code)
    {
        log.debug("GET MESSAGE " + " " + code + " " + accessor.getMessage(code));
        return accessor.getMessage(code);
    }

    public String get(String code, Object[] args)
    {
        log.debug("GET MESSAGE 2 " + messageSource);
        return accessor.getMessage(code, args);
    }
}
