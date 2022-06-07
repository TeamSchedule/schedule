package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import com.schedule.team.model.entity.User;
import com.schedule.team.model.entity.team.Team;
import com.schedule.team.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
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

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task getById(Long id) {
        return taskRepository.getById(id);
    }

    @Override
    public List<Task> getInTimeRange(
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

    @Override
    @Transactional
    public void update(
            Task task,
            String name,
            String description,
            LocalDateTime expirationDate,
            Boolean closed
    ) {
        if (name != null) {
            task.setName(name);
        }
        if (description != null) {
            task.setDescription(description);
        }
        if (expirationDate != null) {
            task.setExpirationTime(expirationDate);
        }
        if (closed != null) {
            task.setClosed(closed);
        }
    }
}
