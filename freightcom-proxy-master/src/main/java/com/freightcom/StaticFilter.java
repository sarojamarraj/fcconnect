package com.freightcom;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class StaticFilter extends ZuulFilter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        return null;
    }

    @Override
    public boolean shouldFilter()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int filterOrder()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public String filterType()
    {
        // TODO Auto-generated method stub
        return "pre";
    }

}
