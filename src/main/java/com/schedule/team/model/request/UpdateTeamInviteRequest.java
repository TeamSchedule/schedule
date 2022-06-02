package com.schedule.team.model.request;

import com.schedule.team.model.TeamInviteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTeamInviteRequest {
    // TODO: validation
    private TeamInviteStatus status;
}
