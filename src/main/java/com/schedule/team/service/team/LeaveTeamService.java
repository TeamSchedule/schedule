package com.schedule.team.service.team;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

public interface LeaveTeamService {
    void leave(PublicTeam team, User user);
}
