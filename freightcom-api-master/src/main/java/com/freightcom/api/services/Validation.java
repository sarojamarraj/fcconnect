package com.freightcom.api.services;

import com.freightcom.api.util.Empty;

public class Validation
{
    private ValidationException exception = null;

    public static Validation get()
    {
        return new Validation();
    }

    public Validation test(boolean test, String variable, String message)
    {
        if (! test) {
            if (exception == null) {
                exception = new ValidationException();
            }

            exception.add(variable, message);
        }

        return this;
    }

    public Validation testNotEmpty(Object value, String variable, String message)
    {
        return test(! Empty.check(value), variable, message);
    }

    public Validation testNotEmpty(Object value, String variable)
    {
        return test(! Empty.check(value), variable, variable + " may not be empty");
    }

    public Validation testNotNull(Object value, String variable, String message)
    {
        return test(value != null, variable, message);
    }

    public Validation testNotNull(Object value, String variable)
    {
        return test(! Empty.check(value), variable, variable + " may not be null");
    }

    public Validation testExists(Object value, String variable)
    {
        return test(! Empty.check(value), variable, variable + " may not be null");
    }

    public void throwIfFailed() throws ValidationException
    {
        if (exception != null) {
            throw exception;
        }
    }


}
