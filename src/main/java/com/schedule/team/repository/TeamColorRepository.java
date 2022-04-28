package com.schedule.team.repository;

import com.schedule.team.model.entity.TeamColor;
import com.schedule.team.model.entity.TeamColorKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamColorRepository extends CrudRepository<TeamColor, TeamColorKey> {
    List<TeamColor> findAllByUserId(Long userId);

    TeamColor findByUserIdAndTeamId(Long userId, Long teamId);
}
