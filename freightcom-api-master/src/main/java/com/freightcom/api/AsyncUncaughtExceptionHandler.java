package com.freightcom.api;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bugsnag.Bugsnag;

public class AsyncUncaughtExceptionHandler implements org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private Bugsnag bugsnag = null;
    private boolean bugsnag_devel = true;

    private final String bugsnag_api_key;

    public AsyncUncaughtExceptionHandler(final String bugsnag_api_key,
                                         boolean bugsnag_devel)
    {
        this.bugsnag_api_key = bugsnag_api_key;
        this.bugsnag_devel = bugsnag_devel;
    }

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... parameters)
    {
        log.debug("ASYNC UNCAUGHT " + throwable);

        if (! bugsnag_devel) {
            if (bugsnag == null) {
                log.debug("ASYNC UNCAUGHT NO BUGSNAG " + throwable);
                bugsnag = new Bugsnag(bugsnag_api_key);
            }


            bugsnag.notify(throwable);
        }


    }

}
