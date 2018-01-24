package com.freightcom.api.services.dataobjects;

import javax.xml.bind.annotation.XmlAttribute;

public class BaseAPIRequest implements ApiCommand
{
    private String command = null;
    private String sessionID = null;
    private String includeMetaData = "yes";

    @XmlAttribute
    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    @XmlAttribute
    public String getSessionID()
    {
        return sessionID;
    }

    public String toString()
    {
        return "<request sessionID=\"" + sessionID + "\" command=\"" + command + "\"/>";
    }

    @XmlAttribute
    public String getIncludeMetaData()
    {
        return includeMetaData;
    }

    @Override
    public void setSessionID(String sessionId)
    {
        this.sessionID = sessionId;        
    }


}
