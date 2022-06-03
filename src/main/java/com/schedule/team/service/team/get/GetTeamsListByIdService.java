package com.schedule.team.service.team.get;

import com.schedule.team.model.entity.team.Team;

import java.util.List;

public interface GetTeamsListByIdService {
    List<Team> get(List<Long> teamsIds);
}
