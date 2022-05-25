package com.schedule.team.repository;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByExpirationTimeBetweenAndTeamIn(
            LocalDateTime expirationTime,
            LocalDateTime expirationTime2,
            Collection<Team> team
    );
}
