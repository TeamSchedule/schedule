package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;

public interface GetTeamColorByUserIdAndTeamIdService {
    TeamColor get(Long userId, Long teamId);
}
