package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

public interface UpdateTaskService {
    @Transactional
    void patchTask(
            Task task,
            String name,
            String description,
            LocalDateTime expirationDate,
            Boolean closed
    );
}
