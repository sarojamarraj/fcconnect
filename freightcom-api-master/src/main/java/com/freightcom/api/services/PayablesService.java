package com.freightcom.api.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.freightcom.api.controllers.transfer.PrintChequeRequest;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.Service;

@Component
public interface PayablesService
{

    Object getPayable(Long payableId);

    Object listPayables(Map<String, Object> criteria, Pageable pageable);

    List<Payable> generatePayables();

    boolean deletePayable(Long payableId);

    void payablesBackgroundProcess(Service.Term term);

    Payable markPaid(Long payableId);

    void runAllPayables();

    Object printCheques(PrintChequeRequest request) throws ValidationException, Exception;

}
