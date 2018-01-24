 package com.freightcom.api;

public class ReportableError extends Error
{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ReportableError()
    {
        // TODO Auto-generated constructor stub
    }

    public ReportableError(String message)
    {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public ReportableError(Throwable cause)
    {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public ReportableError(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public ReportableError(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

}
