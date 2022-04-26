package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;

public interface GetTeamColorService {
    TeamColor get(Long teamId, Long userId);
}
