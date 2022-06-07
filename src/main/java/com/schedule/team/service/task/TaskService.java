package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.Team;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {
    Task create(
            String name,
            User author,
            User assignee,
            Team team,
            String description,
            LocalDateTime creationTime,
            LocalDateTime expirationTime
    );

    void deleteById(Long id);

    Task getById(Long id);

    List<Task> getInTimeRange(
            LocalDateTime from,
            LocalDateTime to,
            Long assigneeId,
            List<Team> teams
    );

    @Transactional
    void update(
            Task task,
            String name,
            String description,
            LocalDateTime expirationDate,
            Boolean closed
    );
}
