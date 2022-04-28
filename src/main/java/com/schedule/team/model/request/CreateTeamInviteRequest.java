package com.schedule.team.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CreateTeamInviteRequest {
    private Long teamId;
    private List<Long> invitedIds;
}
