package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.Team;
import com.schedule.team.model.entity.User;
import com.schedule.team.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetTasksInRangeServiceImpl implements GetTasksInRangeService {
    private final TaskRepository taskRepository;

    @Override
    public List<Task> getTasksInRange(LocalDateTime from, LocalDateTime to, User user, List<Team> teams) {
        return taskRepository.findByExpirationTimeBetweenAndTeamIn(
                from,
                to,
                teams
        );
    }
}
