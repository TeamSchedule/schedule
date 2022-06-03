package com.schedule.team.service.team_color;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

public interface CreateTeamColorService {
    void create(PublicTeam team, User user);
}
