package com.schedule.team.service.team;

import com.schedule.team.model.entity.team.PublicTeam;

import javax.transaction.Transactional;

public interface UpdateTeamService {
    @Transactional
    void update(PublicTeam team, String newName);
}
