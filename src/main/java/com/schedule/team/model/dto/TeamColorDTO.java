package com.schedule.team.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamColorDTO {
    private TeamDTO team;
    private String color;
}
