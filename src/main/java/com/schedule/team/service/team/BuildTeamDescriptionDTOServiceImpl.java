package com.schedule.team.service.team;

import com.schedule.team.model.dto.team.TeamDescriptionDTO;
import com.schedule.team.model.entity.team.PublicTeam;
import org.springframework.stereotype.Service;

@Service
public class BuildTeamDescriptionDTOServiceImpl implements BuildTeamDescriptionDTOService {
    @Override
    public TeamDescriptionDTO build(PublicTeam team, String color) {
        return new TeamDescriptionDTO(
                team.getId(),
                team.getName(),
                team.getCreationDate(),
                team.getAdmin().getId(),
                color
        );
    }
}
