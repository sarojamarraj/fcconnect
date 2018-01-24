package com.freightcom.api.util;

import java.util.ArrayList;
import java.util.List;

public class StringList
{
    private List<String> components = new ArrayList<String>();

    public static StringList get()
    {
        return new StringList();
    }

    private StringList()
    {

    }

    public StringList add(String component)
    {
        if (component != null && ! component.equals("")) {
            components.add(component);
        }

        return this;
    }

    public String join(String separator)
    {
        return String.join(separator, components);
    }

    public static boolean different(List<?> a, List<?> b)
    {
        boolean different = false;

        if (a == null && b == null) {
            different = false;
        } if (a == null && b != null) {
            different = true;
        } else if (a != null && b == null) {
            different = true;
        } else {
            for (Object item: a) {
                if (! b.contains(item)) {
                    different = true;
                    break;
                }
            }

            if (! different) {
                for (Object item: b) {
                    if (! a.contains(item)) {
                        different = true;
                        break;
                    }
                }
            }
        }

        return different;
    }

    public static List<String> convert(Object a) throws Exception
    {
        List<String> converted = null;

        if (a == null) {
            // okay
        } else if (a instanceof List<?>) {
            converted = new ArrayList<String>();

            for (Object item: ((List<?>) a)) {
                if (item instanceof String) {
                    converted.add((String) item);
                } else {
                    throw new Exception("not string list");
                }
            }
        } else {
            throw new Exception("not string list");
        }

        return converted;
    }
}
