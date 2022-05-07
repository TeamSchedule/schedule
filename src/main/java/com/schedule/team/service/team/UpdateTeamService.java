package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;

import javax.transaction.Transactional;

public interface UpdateTeamService {
    @Transactional
    void update(Team team, String newName);
}
