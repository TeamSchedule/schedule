package com.schedule.team.service.team_invite;

import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;

import javax.transaction.Transactional;

public interface PatchTeamInviteService {
    @Transactional
    void patch(TeamInvite teamInvite, TeamInviteStatus teamInviteStatus);
}
