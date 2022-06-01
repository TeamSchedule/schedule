package com.schedule.team.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    private String name;
    private String description;
    private LocalDateTime expirationTime;
    private Long teamId;
    private Long assigneeId;
}
