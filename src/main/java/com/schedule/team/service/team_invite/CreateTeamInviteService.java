package com.schedule.team.service.team_invite;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

import java.time.LocalDateTime;

public interface CreateTeamInviteService {
    void create(Team team, User inviting, User invited, LocalDateTime time);
}
