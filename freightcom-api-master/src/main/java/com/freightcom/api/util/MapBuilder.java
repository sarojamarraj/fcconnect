package com.freightcom.api.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MapBuilder
{
    private final Map<String, Object> map = new HashMap<String, Object>();

    public static MapBuilder getNew()
    {
        return new MapBuilder();
    }

    public static MapBuilder ok()
    {
        return getNew().put("status", "ok");
    }

    public static MapBuilder ok(String arg1, String... args)
    {
        return getNew().put("status", "ok").setValues(arg1, args);
    }

    public static MapBuilder error()
    {
        return getNew().put("status", "error");
    }

    public static MapBuilder error(String arg1, String... args)
    {
        return getNew().put("status", "error").setValues(arg1, args);
    }

    public static Map<String, Object> emptyMap()
    {
        return new HashMap<String, Object>(0);
    }

    public static MapBuilder getNew(String key, Object value)
    {
        MapBuilder builder = new MapBuilder();

        builder.put(key, value);

        return builder;
    }

    private MapBuilder()
    {

    }

    public MapBuilder set(String arg1, String... args)
    {
        return setValues(arg1, args);
    }

    public MapBuilder setValues(String arg1, String[] args)
    {
        String key = arg1;
        int index = 1;

        for (String arg: args) {
            if (index % 2 == 0) {
                key = arg;
            } else {
                this.put(key, arg);
            }

            index += 1;
        }

        return this;
    }

    public MapBuilder put(String key, Object value)
    {
        map.put(key, value);

        return this;
    }

    public Object get(String key)
    {
        return map.get(key);
    }

    public Map<String, Object> getMap()
    {
        return map;
    }

    public Map<String, Object> toMap()
    {
        return map;
    }

    public Collection<Object> values()
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
