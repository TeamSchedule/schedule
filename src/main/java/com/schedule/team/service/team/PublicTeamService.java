package com.schedule.team.service.team;

import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.PublicTeam;

import javax.transaction.Transactional;
import java.time.LocalDate;

public interface PublicTeamService {
    PublicTeam create(String name, LocalDate creationDate, User user);

    @Transactional
    void join(PublicTeam team, User user);

    PublicTeam getById(Long id);

    @Transactional
    void leave(PublicTeam team, User user);

    boolean existsById(Long id);

    @Transactional
    void update(PublicTeam team, String newName);
}
