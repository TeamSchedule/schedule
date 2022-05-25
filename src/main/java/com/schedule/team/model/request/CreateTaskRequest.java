package com.schedule.team.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CreateTaskRequest {
    private String name;
    private String description;
    private LocalDateTime expirationTime;
    private Long teamId;
    private Long assigneeId;
}
