package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

import javax.transaction.Transactional;

public interface JoinTeamService {
    @Transactional
    void join(Team team, User user);
}
