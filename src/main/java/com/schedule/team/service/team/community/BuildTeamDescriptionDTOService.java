package com.schedule.team.service.team.community;

import com.schedule.team.model.dto.team.TeamDescriptionDTO;
import com.schedule.team.model.entity.team.PublicTeam;

public interface BuildTeamDescriptionDTOService {
    TeamDescriptionDTO build(PublicTeam team, String color);
}
