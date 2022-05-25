package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateTaskServiceImpl implements CreateTaskService {
    private final TaskRepository taskRepository;

    @Override
    public Task create(
            String name,
            User author,
            User assignee,
            Team team,
            String description,
            LocalDateTime creationTime,
            LocalDateTime expirationTime
    ) {
        return taskRepository.save(
                new Task(
                        name,
                        author,
                        assignee,
                        team,
                        description,
                        creationTime,
                        expirationTime
                )
        );
    }
}
