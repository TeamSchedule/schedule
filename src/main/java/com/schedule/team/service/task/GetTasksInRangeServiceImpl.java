package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.team.Team;
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
    public List<Task> getTasksInRange(
            LocalDateTime from,
            LocalDateTime to,
            Long assigneeId,
            List<Team> teams
    ) {
        return taskRepository.findByExpirationTimeBetweenAndTeamInAndAssignee_Id(
                from,
                to,
                teams,
                assigneeId
        );
    }
}
