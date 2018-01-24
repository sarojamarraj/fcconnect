package com.freightcom.api.controllers.transfer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.PagedResources;

public class MessagePayload
{
    private List<Object> messages = new ArrayList<Object>();
    private PagedResources<?> payload;

    public MessagePayload(PagedResources<?> payload)
    {
        this.payload = payload;
    }

    public MessagePayload(PagedResources<?> payload, List<Object> messages)
    {
        this.payload = payload;
        addMessages(messages);
    }

    public MessagePayload addMessage(Object message)
    {
        messages.add(message);

        return this;
    }

    public MessagePayload addMessages(List<Object> messages)
    {
        this.messages.addAll(messages);

        return this;
    }

    public Object get_content()
    {
        return payload.getContent();
    }

    public Object getPage()
    {
        return payload.getMetadata();
    }

    public List<Object> getMessages()
    {
        return messages;
    }
    
    public String toString() 
    {
        return "MessagePayload " + payload + " M " + messages.size();
    }
}
