package com.schedule.team.service.user;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.DefaultTeam;

public interface UserService {
    User create(Long id, DefaultTeam defaultTeam);

    User getById(Long id);

    boolean existsById(Long id);
}
