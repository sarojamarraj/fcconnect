package com.freightcom.api.services;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends Exception
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Map<String,String> errors;

    public static ValidationException test(boolean test, String variable, String message) throws ValidationException
    {
        if (test) {
            return null;
        } else {
            throw new ValidationException(variable, message);
        }
    }

    public ValidationException(Map<String,String> errors)
    {
    	super("Validation failed");
        this.errors = errors;
    }

    protected ValidationException()
    {
    	super("Validation failed");
        errors = new HashMap<String,String>();
    }

    protected ValidationException(String variable, String message)
    {
    	super("Validation failed");
        errors = new HashMap<String,String>();

        add(variable, message);
    }

    public Map<String,String> getErrors()
    {
        return errors;
    }

    public static ValidationException get()
    {
        return new ValidationException();
    }

    public static ValidationException get(String variable, String message)
    {
        return new ValidationException(variable, message);
    }

    public ValidationException add(String property, String message)
    {
        errors.put(property, message);

        return this;
    }

    public void doThrow() throws ValidationException
    {
        throw this;
    }

    @Override
    public String getMessage()
    {
        StringBuilder builder = new StringBuilder();
        String separator = "";

        builder.append("Validation failed: ");

        for(String key: errors.keySet()) {
            builder
                .append(separator)
                .append(key)
                .append(": ")
                .append(errors.get(key));

            separator = ", ";
        }

        return builder.toString();
    }
}
