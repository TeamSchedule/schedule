package com.schedule.team.service.team.community;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

import javax.transaction.Transactional;

public interface JoinTeamService {
    @Transactional
    void join(PublicTeam team, User user);
}
