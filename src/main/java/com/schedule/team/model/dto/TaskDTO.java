package com.schedule.team.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.schedule.team.model.dto.team.TeamShortDescriptionDTO;
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
    private TeamShortDescriptionDTO team;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime creationTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime expirationTime;
    private boolean closed;
}
