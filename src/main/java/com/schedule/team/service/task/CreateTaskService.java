package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

import java.time.LocalDateTime;

public interface CreateTaskService {
    Task create(
            String name,
            User author,
            User assignee,
            Team team,
            String description,
            LocalDateTime creationTime,
            LocalDateTime expirationTime
    );
}
