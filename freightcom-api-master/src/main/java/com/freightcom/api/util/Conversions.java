package com.freightcom.api.util;


public class Conversions
{
    public static String toString(Object value)
    {
        if (value == null) {
            return null;
        } else {
            return value.toString();
        }
    }
}
