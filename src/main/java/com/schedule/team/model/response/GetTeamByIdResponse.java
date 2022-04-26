package com.schedule.team.model.response;

import com.schedule.team.model.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamByIdResponse {
    private Team team;
    private String color;
}
