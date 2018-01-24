package com.freightcom.api.model.support;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderStatusCodeTest
{
    
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void test1()
    {
        for (OrderStatusCode status: OrderStatusCode.values())
        {
            System.out.println("S " + status + " name = " + status.name() + " " + status.getValue() + " " + status.ordinal());
        }
    }

    @Test
    public void test2()
    {
       System.out.println("S2 " + OrderStatusCode.valueOf("WAITING_BORDER"));
    }

    @Test
    public void test3() throws Exception
    {
       System.out.println("S3 " + OrderStatusCode.get("WAITING BORDER"));
    }

    @Test
    public void test4() throws Exception
    {
       System.out.println("S4 " + OrderStatusCode.get(1L));
    }

    @Test
    public void test5() throws Exception
    {
       System.out.println("S5 " + OrderStatusCode.get(new Long(18)));
    }

    @Test
    public void test6() throws Exception
    {
        assertEquals(1,1);
    }

}
