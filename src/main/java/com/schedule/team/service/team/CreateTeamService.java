package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

import java.time.LocalDate;

public interface CreateTeamService {
    Team create(String name, LocalDate creationDate, User user);
}
