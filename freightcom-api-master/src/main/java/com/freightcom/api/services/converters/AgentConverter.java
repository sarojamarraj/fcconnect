package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.AgentListView;

public class AgentConverter implements Converter<UserRole, AgentListView>
{

    @Override
    public AgentListView convert(UserRole agent)
    {
        try {
            return new AgentListView(agent.asAgent());
        } catch (Exception e) {
            return null;
        }
    }

}
