package com.schedule.team.service.team_invite;

import com.schedule.team.model.dto.TeamInviteDTO;
import com.schedule.team.model.entity.TeamInvite;

public interface BuildTeamInviteDTOService {
    TeamInviteDTO build(TeamInvite teamInvite);
}
