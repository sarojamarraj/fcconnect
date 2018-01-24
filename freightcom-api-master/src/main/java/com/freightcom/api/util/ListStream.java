package com.freightcom.api.util;

import java.util.ArrayList;
import java.util.List;

public class ListStream<T>
{
    private List<T> components = new ArrayList<T>();

    public ListStream()
    {

    }

    public  ListStream<T> add(T component)
    {
        if (component != null && ! component.equals("")) {
            components.add(component);
        }

        return this;
    }

    public List<T> toList()
    {
        return components;
    }
}
