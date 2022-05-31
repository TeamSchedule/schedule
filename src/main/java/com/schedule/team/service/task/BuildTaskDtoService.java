package com.schedule.team.service.task;

import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.entity.Task;

public interface BuildTaskDtoService {
    TaskDTO build(Task task);
}
