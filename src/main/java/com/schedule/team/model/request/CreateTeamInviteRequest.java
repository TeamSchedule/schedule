package com.schedule.team.model.request;

import com.schedule.team.validation.constraint.UserIsAlreadyInvitedConstraint;
import com.schedule.team.validation.constraint.UserIsAlreadyTeamMemberConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UserIsAlreadyTeamMemberConstraint
@UserIsAlreadyInvitedConstraint
public class CreateTeamInviteRequest {
    private Long teamId;
    private List<Long> invitedIds;
}
