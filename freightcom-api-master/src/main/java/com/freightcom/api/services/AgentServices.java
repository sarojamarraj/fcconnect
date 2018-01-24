package com.freightcom.api.services;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.freightcom.api.model.Agent;
import com.freightcom.api.model.UserRole;

public interface AgentServices
{

    Agent getAgent(Long agentId);

    Page<UserRole> listAgents(Map<String, String> criteria, Pageable pageable);

    Agent update(Long agentId, Agent agentData, Map<String, Object> attributes) throws ValidationException, AccessDeniedException;

}
