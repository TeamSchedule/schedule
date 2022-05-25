package com.schedule.team.service.task;

import com.schedule.team.model.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateTaskServiceImpl implements UpdateTaskService {
    @Override
    @Transactional
    public void patchTask(
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
