package com.schedule.team.service.team;

import com.schedule.team.model.dto.team.TeamDescriptionDTO;
import com.schedule.team.model.entity.Team;

public interface BuildTeamDescriptionDTOService {
    TeamDescriptionDTO build(Team team, String color);
}
