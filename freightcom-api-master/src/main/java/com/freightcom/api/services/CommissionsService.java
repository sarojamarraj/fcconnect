package com.freightcom.api.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.Agent;
import com.freightcom.api.model.CommissionPayable;

@Component
public interface CommissionsService
{

    Object getCommission(Long commissionId);

    Object listCommissions(Map<String, Object> criteria, Pageable pageable);

    List<CommissionPayable> calculateCommissions();

    Object deleteCommission(Long commissionPayableId);

    List<CommissionPayable> calculateCommissions(Long agentId);

    void commissionsBackgroundProcess(Agent.Term term);

    CommissionPayable markPaid(Long commissionPayableId);

    void runAllCommissions();

}
