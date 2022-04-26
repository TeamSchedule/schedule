package com.schedule.team.model.response;

import com.schedule.team.model.dto.TeamColorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamsResponse {
    private List<TeamColorDTO> teamColors;
}
