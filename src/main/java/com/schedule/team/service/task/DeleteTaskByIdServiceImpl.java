package com.schedule.team.service.task;

import com.schedule.team.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteTaskByIdServiceImpl implements DeleteTaskByIdService {
    private final TaskRepository taskRepository;

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
