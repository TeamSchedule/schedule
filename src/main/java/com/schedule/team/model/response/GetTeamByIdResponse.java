package com.schedule.team.model.response;

import com.schedule.team.model.TeamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamByIdResponse {
    private TeamDTO team;
    private String color;
}
