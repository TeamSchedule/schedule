package com.schedule.team.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeamInviteRequest {
    private Long teamId;
    private List<Long> invitedIds;
}
