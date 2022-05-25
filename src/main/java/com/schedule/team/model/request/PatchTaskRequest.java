package com.schedule.team.model.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatchTaskRequest {
    private String name;
    private String description;
    private LocalDateTime expirationDate;
    private Boolean closed;
}
