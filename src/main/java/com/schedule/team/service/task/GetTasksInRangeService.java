package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface GetTasksInRangeService {
    List<Task> getTasksInRange(LocalDateTime from, LocalDateTime to, User user, List<Team> teams);
}
