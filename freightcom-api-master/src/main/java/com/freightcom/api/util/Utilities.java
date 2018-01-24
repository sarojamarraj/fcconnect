package com.freightcom.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Utilities
{
    public static String getStackTrace(Throwable e)
    {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        return writer.toString();
    }
}
