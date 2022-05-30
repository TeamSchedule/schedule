package com.schedule.team.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String name;
    private Long authorId;
    private Long assigneeId;
    private TeamDTO team;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime creationTime;
    @JsonFormat(pattern = "yyyy-MM-dd@HH:mm:ss")
    private LocalDateTime expirationTime;
    private boolean closed;
}
