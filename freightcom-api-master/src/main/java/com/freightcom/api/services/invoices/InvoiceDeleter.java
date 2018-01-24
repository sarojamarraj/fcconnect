package com.freightcom.api.services.invoices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.Invoice;
import com.freightcom.api.repositories.ChargeRepository;
import com.freightcom.api.repositories.ObjectBase;

/**
 * @author bryan
 *
 */
@Component
public class InvoiceDeleter
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public InvoiceDeleter(final ChargeRepository chargeRepository,
                          final ObjectBase objectBase)
    {
    }

    public void delete(Invoice invoice)
    {

    }
}
