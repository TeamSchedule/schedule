package com.schedule.team.service.team_invite;

import com.schedule.team.model.GetTeamInviteCriteria;
import com.schedule.team.model.entity.TeamInvite;

import java.util.List;

public interface GetTeamInvitesByUserService {
    List<TeamInvite> get(Long userId, GetTeamInviteCriteria getTeamInviteCriteria);
}
