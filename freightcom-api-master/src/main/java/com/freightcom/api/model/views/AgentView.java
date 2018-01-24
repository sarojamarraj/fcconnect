package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Agent;

@Relation(collectionRelation = "agent")
public class AgentView extends AgentViewCommon
{


    public AgentView(Agent agent) throws Exception
    {
        super(agent);
    }

}
