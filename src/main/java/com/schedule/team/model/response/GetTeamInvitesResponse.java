package com.schedule.team.model.response;

import com.schedule.team.model.dto.TeamInviteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTeamInvitesResponse {
    private List<TeamInviteDTO> teamInvites;
}
