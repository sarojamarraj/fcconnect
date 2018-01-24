package com.freightcom.api.controllers;


public class ActionResponse
{
    private String status = "ok";
    private String message = "";

    public ActionResponse()
    {

    }

    public ActionResponse(String message)
    {
        this.setMessage(message);
    }

    public ActionResponse(boolean ok, String message)
    {
        this.setMessage(message);

        if (ok) {
            this.setStatus("ok");
        } else {
            this.setStatus("fail");
        }
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }


}
