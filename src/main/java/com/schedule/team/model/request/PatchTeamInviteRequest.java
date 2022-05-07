package com.schedule.team.model.request;

import com.schedule.team.model.TeamInviteStatus;
import lombok.Data;

@Data
public class PatchTeamInviteRequest {
    private Long id;
    // TODO: validation
    private TeamInviteStatus status;
}
