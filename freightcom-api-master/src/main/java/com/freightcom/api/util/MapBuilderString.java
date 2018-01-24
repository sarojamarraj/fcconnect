package com.freightcom.api.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapBuilderString
{
    private final Map<String, String> map = new HashMap<String, String>();

    public static MapBuilderString getNew()
    {
        return new MapBuilderString();
    }

    public static Map<String, String> emptyMap()
    {
        return new HashMap<String, String>(0);
    }

    public static MapBuilderString getNew(String key, String value)
    {
        MapBuilderString builder = new MapBuilderString();

        builder.put(key, value);

        return builder;
    }

    private MapBuilderString()
    {

    }

    public MapBuilderString put(String key, String value)
    {
        map.put(key, value);

        return this;
    }

    public String get(String key)
    {
        return map.get(key);
    }

    public Map<String, String> getMap()
    {
        return map;
    }

    public Map<String, String> toMap()
    {
        return map;
    }

    public Collection<String> values()
    {
        return map.values();
    }

    public static String toString(Map<?,?> map)
    {
        StringBuilder builder = new StringBuilder();

        for (Object key : map.keySet()) {
            builder.append(key)
                .append(" => ")
                .append(map.get(key))
                .append("\n");
        }

        return builder.toString();
    }

    public String toString()
    {
        return toString(map);
    }
}
