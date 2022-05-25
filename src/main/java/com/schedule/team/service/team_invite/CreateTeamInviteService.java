package com.schedule.team.service.team_invite;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.TeamInvite;
import com.schedule.team.model.entity.User;

import java.time.LocalDateTime;

public interface CreateTeamInviteService {
    TeamInvite create(Team team, User inviting, User invited, LocalDateTime time);
}
