package com.freightcom.api.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

import com.freightcom.api.model.Invoice;

public class InvoiceLock
{
    private final static Logger log = LoggerFactory.getLogger(InvoiceLock.class);

    private static final int MAX_ENTRIES = 1000;
    private static int counter = 0;
    private final int id;
    private final long invoiceId;

    private static Map<Long, InvoiceLock> locks = new LinkedHashMap<Long, InvoiceLock>(MAX_ENTRIES + 1, .75F, true) {
        /**
        *
        */
        private static final long serialVersionUID = 1L;

        public boolean removeEldestEntry(Map.Entry<Long, InvoiceLock> eldest)
        {
            return size() > MAX_ENTRIES;
        }
    };

    private InvoiceLock(Long invoiceId)
    {
        id = counter++;
        this.invoiceId = invoiceId;
    }

    public String toString()
    {
        return "INVOICE LOCK " + invoiceId + " " + id;
    }

    public static InvoiceLock getLock(Invoice invoice)
    {
        if (invoice == null) {
            return getLock(0L);
        } else {
            return getLock(invoice.getId());
        }
    }

    public static synchronized InvoiceLock getLock(Long id)
    {
        InvoiceLock lock = locks.get(id);

        if (lock == null) {
            lock = new InvoiceLock(id);

            locks.put(id, lock);
        }

        log.debug("GOT " + lock);

        return lock;
    }
}
