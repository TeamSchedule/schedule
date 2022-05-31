package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.TeamInviteStatus;
import com.schedule.team.model.entity.TeamInvite;

import java.util.List;

public interface GetTeamInvitesService {
    List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status,
            Long teamId
    );

    List<TeamInvite> get(
            Long userId,
            GetTeamInviteCriteria criteria,
            TeamInviteStatus status
    );
}
