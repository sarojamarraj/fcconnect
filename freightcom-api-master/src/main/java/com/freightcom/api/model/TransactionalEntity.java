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

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 * The parent class for all transactional persistent entities.
 *
 * @author
 */
@MappedSuperclass
public abstract class TransactionalEntity implements Serializable
{
    @Transient
    private final Logger debug_logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The default serial version UID.
     */
    private static final long serialVersionUID = 1L;

    public abstract Long getId();

    public abstract void setId(final Long id);

    public String getReferenceId()
    {
        return "";
    }

    public void setReferenceId(final String referenceId)
    {

    }

    // public Integer getVersion() {
    // return version;
    // }
    //
    // public void setVersion(final Integer version) {
    // this.version = version;
    // }

    public String getCreatedBy()
    {
        return null;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setCreatedBy(final String createdBy)
    {

    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return null;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setCreatedAt(final Date createdAt)
    {

    }

    @JsonIgnore
    public ZonedDateTime getDeletedAt()
    {
        return null;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setDeletedAt(final Date DeletedAt)
    {

    }

    @JsonProperty(access = Access.READ_ONLY)
    @JsonIgnore
    public String getUpdatedBy()
    {
        return null;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setUpdatedBy(final String updatedBy)
    {

    }

    @JsonIgnore
    public ZonedDateTime getUpdatedAt()
    {
        return null;
    }

    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    public void setUpdatedAt(final Date updatedAt)
    {

    }

    @JsonIgnore
    @JsonProperty(access = Access.READ_ONLY)
    public void setUpdatedAt(final ZonedDateTime updatedAt)
    {

    }

    /**
     * A listener method which is invoked on instances of TransactionalEntity
     * (or their subclasses) prior to initial persistence. Sets the
     * <code>created</code> audit values for the entity. Attempts to obtain this
     * thread's instance of a username from the RequestContext. If none exists,
     * throws an IllegalArgumentException. The username is used to set the
     * <code>createdBy</code> value. The <code>createdAt</code> value is set to
     * the current timestamp.
     */
    @PrePersist
    public void beforePersist()
    {
        debug_logger.debug("BEFORE PERSIST " + this);
        if (getCreatedAt() == null) {
            debug_logger.debug("BEFORE PERSIST WAS NULL " + this);
            setCreatedAt(new Date());
        }

        setUpdatedAt(this.getCreatedAt());
    }

    /**
     * A listener method which is invoked on instances of TransactionalEntity
     * (or their subclasses) prior to being updated. Sets the
     * <code>updated</code> audit values for the entity. Attempts to obtain this
     * thread's instance of username from the RequestContext. If none exists,
     * throws an IllegalArgumentException. The username is used to set the
     * <code>updatedBy</code> value. The <code>updatedAt</code> value is set to
     * the current timestamp.
     */
    @PreUpdate
    public void beforeUpdate()
    {
        debug_logger.debug("BEFORE UPDATE " + this);
        setUpdatedAt(new Date());
    }

    // @PreRemove
    public void beforeRemove()
    {
        debug_logger.debug("BEFORE DELETE " + this);
        // setDeletedAt(new Date());
    }

    /**
     * Determines the equality of two TransactionalEntity objects. If the
     * supplied object is null, returns false. If both objects are of the same
     * class, and their <code>id</code> values are populated and equal, return
     * <code>true</code>. Otherwise, return <code>false</code>.
     *
     * @param that
     *            An Object
     * @return A boolean
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object that)
    {
        if (that == null) {
            return false;
        }

        if (this.getClass()
                .equals(that.getClass())) {
            final TransactionalEntity thatEntity = (TransactionalEntity) that;
            if (this.getId() == null || thatEntity.getId() == null) {
                return false;
            }

            if (this.getId()
                    .equals(thatEntity.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the hash value of this object.
     *
     * @return An int
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        if (getId() == null) {
            return -1;
        }
        return getId().hashCode();
    }

    public ZonedDateTime asDate(Date date)
    {
        if (date == null) {
            return null;
        } else {
            return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.of("UTC"));
        }
    }

    public Date now()
    {
        return legacyDate(ZonedDateTime.now(ZoneId.of("UTC")));
    }

    public ZonedDateTime asDate(ZonedDateTime date)
    {
        return date;
    }

    public Date legacyDate(Date date)
    {
        return date;
    }

    public Date legacyDate(ZonedDateTime date)
    {
        if (date == null) {
            return null;
        } else {
            return Date.from(date.toInstant());
        }
    }

    public boolean patchProperty(String key, TransactionalEntity source, Object value) throws Exception
    {
        return false;
    }

    @SuppressWarnings("unchecked")
    public <T extends TransactionalEntity> T patch(T data, Map<?, ?> attributes) throws Exception
    {
        if (!getClass().isInstance(data)) {
            throw new Exception("Class mismatch in patch " + this + " " + data);
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(data);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(this);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (Object keyObject : attributes.keySet()) {
            if (!(keyObject instanceof String)) {
                throw new Exception("Invalid key " + keyObject);
            }

            String key = (String) keyObject;
            Object value = source.getPropertyValue(key);

            if (dest.isWritableProperty(key)) {
                boolean handled = patchProperty(key, data, attributes.get(key));

                if (handled) {
                    // Handled by patch property
                } else if (key.equals("id")) {
                    // Don't copy ids
                } else if (value instanceof List) {
                    if (dest.getPropertyValue(key) == null) {
                        dest.setPropertyValue(key, value);
                    } else if (!(dest.getPropertyValue(key) instanceof List)) {
                        throw new Exception("PATCH LIST MISMATCH " + this + " " + data);
                    } else {
                        List<?> newValues = (List<?>) value;
                        int n = newValues.size();

                        List<?> existing = (List<?>) dest.getPropertyValue(key);
                        int m = existing.size();
                        int i = 0;

                        while (i < n && i < m) {
                            if (newValues.get(i) instanceof TransactionalEntity
                                    && existing.get(i) instanceof TransactionalEntity) {
                                ((TransactionalEntity) existing.get(i)).patch((TransactionalEntity) newValues.get(i),
                                        (Map<?, ?>) ((List<?>) attributes.get(key)).get(i));
                            } else {
                                throw new Exception("Type mismatch in patch");
                            }

                            i++;
                        }

                       // TODO - no generic add, use patchProperty?
                    }
                } else if (value instanceof TransactionalEntity
                        && dest.getPropertyValue(key) instanceof TransactionalEntity) {
                    // Recursively patch
                    if (attributes.get(key) instanceof Map<?, ?>) {
                        ((TransactionalEntity) dest.getPropertyValue(key)).patch((TransactionalEntity) value,
                                (Map<?, ?>) attributes.get(key));
                    } else {
                        throw new Exception("Bad attribute value for " + key + " in " + this);
                    }
                } else {
                    dest.setPropertyValue(key, value);
                }
            } else {
                throw new Exception("Cannot write property " + key + " in " + this);
            }
        }

        return (T) this;
    }
}
