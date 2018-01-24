package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Agent;

@Relation(collectionRelation = "agent")
public class AgentListView extends AgentViewCommon
{
    public AgentListView(Agent agent) throws Exception
    {
        super(agent);
    }
}
