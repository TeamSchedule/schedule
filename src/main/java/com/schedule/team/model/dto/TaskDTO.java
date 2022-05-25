package com.schedule.team.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String name;
    private Long authorId;
    private Long assigneeId;
    private Long teamId;
    private String description;
    private LocalDateTime creationTime;
    private LocalDateTime expirationTime;
    private boolean closed;
}
