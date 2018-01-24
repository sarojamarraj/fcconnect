/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/
package com.freightcom.api.model;

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.rest.core.annotation.RestResource;

/**
 *
 *
 * @author
 */
@Entity(name = "LoggedEvent")
@Table(name = "logged_event")
public class LoggedEvent extends TransactionalEntity
{
    public static String MESSAGE_TYPE_STATUS = "status";
    public static String MESSAGE_TYPE_ADMIN = "admin";
    public static String MESSAGE_TYPE_ORDER = "order";
    public static String MESSAGE_TYPE_BILLING_STATUS = "billing status";
    public static String MESSAGE_TYPE_INVOICE = "invoice";

    public static String ENTITY_TYPE_ORDER = "order";
    public static String ENTITY_TYPE_INVOICE = "invoice";
    public static String ENTITY_TYPE_CLAIM = "claim";
    public static String ENTITY_TYPE_CRM = "crm";

    private static final long serialVersionUID = 1L;

    public static LoggedEvent orderStatusMessage(User user,
                                                 CustomerOrder order,
                                                 String comment,
                                                 String message)
    {
        return orderStatusMessage(user, order, comment, message,
                                  MessageAction.UPDATE);
    }

    public static LoggedEvent orderStatusMessage(User user,
                                                 CustomerOrder order,
                                                 String comment,
                                                 String message,
                                                 MessageAction action)
    {
        LoggedEvent loggedEvent = new LoggedEvent();

        loggedEvent.setEntityType(ENTITY_TYPE_ORDER);
        loggedEvent.setMessageType(MESSAGE_TYPE_STATUS);
        loggedEvent.setComment(comment);
        loggedEvent.setMessage(order.getOrderStatusName());
        loggedEvent.setEntityId(order.getId());
        loggedEvent.setAction(action);

        if (user != null) {
            loggedEvent.setUserId(user.getId());
        }

        return loggedEvent;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "private")
    private Boolean nPrivate = false;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "user_id")
    private Long userId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "comment")
    private String comment;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private MessageAction action = MessageAction.NONE;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "message")
    private String message;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = true)
    private User user;

    public LoggedEvent()
    {
        super();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Boolean getPrivate()
    {
        return nPrivate;
    }

    public void setPrivate(Boolean nPrivate)
    {
        this.nPrivate = nPrivate;
    }

    public Boolean getNPrivate()
    {
        return nPrivate;
    }

    public void setNPrivate(Boolean nPrivate)
    {
        this.nPrivate = nPrivate;
    }

    public String getEntityType()
    {
        return entityType;
    }

    public void setEntityType(String entityType)
    {
        this.entityType = entityType;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Long getEntityId()
    {
        return entityId;
    }

    public void setEntityId(Long entityId)
    {
        this.entityId = entityId;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public MessageAction getAction()
    {
        return action == null ? MessageAction.NONE : action;
    }

    public void setAction(MessageAction action)
    {
        this.action = action;
    }

    @RestResource(exported = false)
    public User getUser()
    {
        return user;
    }

}
