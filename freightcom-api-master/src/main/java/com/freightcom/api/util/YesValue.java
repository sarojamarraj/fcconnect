package com.freightcom.api.util;


public class YesValue
{
    public static boolean parse(Object value)
    {
        if (value == null) {
            return false;
        } else {
            return value.toString().equalsIgnoreCase("yes")
                || value.toString().equalsIgnoreCase("1")
                || value.toString().equalsIgnoreCase("true");
        }
    }

    public static Boolean parseStrict(Object value)
    {
        if (value == null) {
            return null;
        } else if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value.toString().equalsIgnoreCase("no")
                   || value.toString().equalsIgnoreCase("0")
                   || value.toString().equalsIgnoreCase("false")) {
            return false;
        } else if ( value.toString().equalsIgnoreCase("yes")
                    || value.toString().equalsIgnoreCase("1")
                    || value.toString().equalsIgnoreCase("true")) {
            return true;
        } else {
            return null;
        }
    }
}
