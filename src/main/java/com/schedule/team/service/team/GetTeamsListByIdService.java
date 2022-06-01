package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;

import java.util.List;

public interface GetTeamsListByIdService {
    List<Team> get(List<Long> teamsIds);
}
