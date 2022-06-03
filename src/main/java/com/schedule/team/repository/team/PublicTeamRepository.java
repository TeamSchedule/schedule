package com.schedule.team.repository.team;

import com.schedule.team.model.entity.team.PublicTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicTeamRepository extends JpaRepository<PublicTeam, Long> {
}
