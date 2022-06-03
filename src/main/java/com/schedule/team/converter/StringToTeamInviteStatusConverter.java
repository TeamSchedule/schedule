package com.schedule.team.converter;

import com.schedule.team.model.TeamInviteStatus;
import org.springframework.core.convert.converter.Converter;

public class StringToTeamInviteStatusConverter
        implements Converter<String, TeamInviteStatus> {
    @Override
    public TeamInviteStatus convert(String source) {
        return TeamInviteStatus.valueOf(source.toLowerCase());
    }
}
