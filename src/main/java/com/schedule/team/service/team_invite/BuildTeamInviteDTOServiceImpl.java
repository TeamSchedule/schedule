package com.schedule.team.service.team_invite;

import com.schedule.team.model.dto.TeamInviteDTO;
import com.schedule.team.model.dto.team.TeamShortDescriptionDTO;
import com.schedule.team.model.entity.TeamInvite;
import org.springframework.stereotype.Service;

@Service
public class BuildTeamInviteDTOServiceImpl implements BuildTeamInviteDTOService {
    @Override
    public TeamInviteDTO build(TeamInvite teamInvite) {
        return new TeamInviteDTO(
                teamInvite.getId(),
                teamInvite.getInviting().getId(),
                teamInvite.getInvited().getId(),
                teamInvite.getDate(),
                teamInvite.getInviteStatus(),
                new TeamShortDescriptionDTO(
                        teamInvite.getTeam().getId(),
                        teamInvite.getTeam().getName()
                )
        );
    }
}
