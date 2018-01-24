package com.freightcom.api.model.views;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.User;

@Relation(collectionRelation = "loggedEvents")
public class LoggedEventView implements View
{
    private final LoggedEvent loggedEvent;

    public LoggedEventView(LoggedEvent loggedEvent)
    {
        this.loggedEvent = loggedEvent;
    }

    public Long getId()
    {
        return loggedEvent.getId();
    }

    public Long getEntityId()
    {
        return loggedEvent.getEntityId();
    }

    public String getMessage()
    {
        return loggedEvent.getMessage();
    }

    public String getMessageType()
    {
        return loggedEvent.getMessageType();
    }

    public String getAction()
    {
        return loggedEvent.getAction().toString();
    }

    public String getComment()
    {
        return loggedEvent.getComment();
    }

    public String getCreatedAt()
    {
        return loggedEvent.getCreatedAt() == null ? null : loggedEvent.getCreatedAt()
                .withZoneSameInstant(ZoneId.of("America/Toronto")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm z"));
    }

    public Map<String, Object> getUser()
    {
        Map<String, Object> userInfo = new HashMap<String, Object>(3);
        User user = loggedEvent.getUser();

        if (user != null) {
            userInfo.put("id", user.getId());
            userInfo.put("firstname", user.getFirstname());
            userInfo.put("lastname", user.getLastname());
        }

        return userInfo;
    }

    public String getEntityType()
    {
        return loggedEvent.getEntityType();
    }

    public Boolean getPrivate()
    {
        return loggedEvent.getNPrivate();
    }
}
