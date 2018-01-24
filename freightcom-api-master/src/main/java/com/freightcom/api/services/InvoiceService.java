package com.freightcom.api.services;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;

import com.freightcom.api.model.DeletedInvoice;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.views.InvoiceIndividualView;
import com.freightcom.api.model.views.InvoiceView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.services.dataobjects.InvoicePayment;

public interface InvoiceService extends ServiceProvider
{

    PagedResources<Resource<View>> getInvoicesConverted(Map<String, Object> criteria, Pageable pageable);

    Invoice createOrUpdateInvoice(Invoice invoice) throws Exception;

    /**
     * @param attributes
     * @throws Exception
     *
     */
    View createInvoice(Invoice invoiceData, Map<String, Object> attributes) throws Exception;

    /**
     *
     */
    InvoiceIndividualView updateInvoice(Long id, Invoice invoice, Map<String, Object> attributes) throws Exception;

    /**
     *
     */
    Object deleteInvoices(Map<String, Object> values);

    InvoiceIndividualView view(Long id);

    Invoice findOne(Long id);

    List<InvoiceView> applyCredit(Map<String, Object> attributes) throws AccessDeniedException;

    Object pay(InvoicePayment payment) throws AccessDeniedException;

    Object realDeleteInvoice(Long id) throws Exception;

    List<DeletedInvoice> deletedInvoices();

    InvoiceView getInvoiceView(Long invoiceId);

    View getIndividualInvoiceView(Long invoiceId);

    Object payNow(InvoicePayment payment) throws AccessDeniedException;

    void autoInvoiceDaily() throws Exception;

    void autoInvoicWeekly() throws Exception;

    void autoInvoiceBiWeekly() throws Exception;

    void autoInvoiceMonthly() throws Exception;

    void autoInvoiceAll() throws Exception;

    Object calculateCharges(Stream<Long> map) throws Exception;

    Object viewApplyCredit(Long id);
}
