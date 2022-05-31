package com.schedule.team.service.task;

import com.schedule.team.model.dto.TaskDTO;
import com.schedule.team.model.dto.team.TeamShortDescriptionDTO;
import com.schedule.team.model.entity.Task;
import org.springframework.stereotype.Service;

@Service
public class BuildTaskDtoServiceImpl implements BuildTaskDtoService {
    @Override
    public TaskDTO build(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getName(),
                task.getAuthor().getId(),
                task.getAssignee().getId(),
                new TeamShortDescriptionDTO(
                        task.getTeam().getId(),
                        task.getTeam().getName()
                ),
                task.getDescription(),
                task.getCreationTime(),
                task.getExpirationTime(),
                task.isClosed()
        );
    }
}
