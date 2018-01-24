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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *
 * @author
 */
@Entity(name = "SystemLog")
@Table(name = "system_log")
public class SystemLog extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="object_id")
    private Long objectId;

    @Column(name="action")
    private String action;

    @Column(name="comments")
    private String comments;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    public SystemLog() {
        super();
    }

    public Long getId() {
         return id;
    }

    public void setId(Long id) {
         this.id = id;
    }

    public Long getUserId() {
         return userId;
    }

    public void setUserId(Long userId) {
         this.userId = userId;
    }

    public Long getObjectId() {
         return objectId;
    }

    public void setObjectId(Long objectId) {
         this.objectId = objectId;
    }

    public String getAction() {
         return action;
    }

    public void setAction(String action) {
         this.action = action;
    }

    public String getComments() {
         return comments;
    }

    public void setComments(String comments) {
         this.comments = comments;
    }

    public ZonedDateTime getCreatedAt() {
         return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt) {
         this.createdAt = createdAt;
    }

}
