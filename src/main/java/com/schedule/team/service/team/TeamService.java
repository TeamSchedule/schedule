package com.schedule.team.service.team;

import com.schedule.team.model.entity.team.Team;

import java.util.List;

public interface TeamService {
    Team getById(Long id);
    List<Team> getListByIds(List<Long> teamsIds);
}
