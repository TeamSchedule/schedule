package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.TeamColor;

import java.util.List;

public interface GetTeamColorsByUserIdService {
    List<TeamColor> get(Long userId);
}
