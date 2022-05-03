package com.schedule.team.converter;

import com.schedule.team.model.GetTeamInviteCriteria;
import org.springframework.core.convert.converter.Converter;

public class StringToGetTeamInviteCriteriaConverter
        implements Converter<String, GetTeamInviteCriteria> {
    @Override
    public GetTeamInviteCriteria convert(String source) {
        return GetTeamInviteCriteria.valueOf(source.toUpperCase());
    }
}
