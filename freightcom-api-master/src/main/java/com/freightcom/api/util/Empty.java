package com.freightcom.api.util;

public class Empty
{
    public static boolean check(Object value)
    {
        return value == null || value.toString()
                .equals("")
                || value.toString()
                        .matches("/^\\s*$/");
    }

    public static <T> T ifNull(T object, T defaultValue)
    {
        return object == null ? defaultValue : object;
    }

    public static String asString(Object object, String defaultValue)
    {
        if (object instanceof String) {
            return (String) object;
        }

        if (object == null) {
            return defaultValue;
        }

        return object.toString();
    }

    public static String asString(Object object)
    {
        return asString(object, "");
    }
}
