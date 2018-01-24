package com.freightcom.api.util;


public class StringUtil
{
    public static boolean check(Object value)
    {
        return value == null
            || value.toString().matches("/^\\s*$/");
    }

    public static String substring(String string, int start)
    {
        return substring(string, start, -1);
    }

    public static String substring(String string, int start, int count)
    {
        if (string == null || count == 0) {
            return "";
        } else {
            int n = string.length();

            if (start < 0) {
                start = n + start;

                if (start < 0) {
                    start = 0;
                    count = n;
                } else if (count < 0) {
                    count = n - start;
                }
            } else if (count < 0) {
                count = n - start;
            }

            if (start + count > n) {
                count = n - start;
            }

            if (count <= 0) {
                return "";
            } else {
                return string.substring(start, n - count);
            }
        }
    }
}
