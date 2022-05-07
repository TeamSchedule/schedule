package com.schedule.team.service.team;

import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

public interface LeaveTeamService {
    void leave(Team team, User user);
}
