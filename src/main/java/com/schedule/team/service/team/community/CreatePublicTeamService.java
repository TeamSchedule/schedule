package com.schedule.team.service.team.community;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

import java.time.LocalDate;

public interface CreatePublicTeamService {
    PublicTeam create(String name, LocalDate creationDate, User user);
}
