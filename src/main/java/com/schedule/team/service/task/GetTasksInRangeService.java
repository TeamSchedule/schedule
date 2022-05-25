package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;

import java.time.LocalDateTime;
import java.util.List;

public interface GetTasksInRangeService {
    List<Task> getTasksInRange(LocalDateTime from, LocalDateTime to, Long assigneeId, List<Team> teams);
}
