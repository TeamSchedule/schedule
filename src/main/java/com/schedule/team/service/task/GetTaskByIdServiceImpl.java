package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetTaskByIdServiceImpl implements GetTaskByIdService {
    private final TaskRepository taskRepository;

    @Override
    public Task get(Long id) {
        return taskRepository.getById(id);
    }
}
