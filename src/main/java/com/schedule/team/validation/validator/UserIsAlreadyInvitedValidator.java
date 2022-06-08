package com.schedule.team.validation.validator;

import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.service.team_invite.TeamInviteService;
import com.schedule.team.validation.constraint.UserIsAlreadyInvitedConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UserIsAlreadyInvitedValidator
        implements ConstraintValidator<UserIsAlreadyInvitedConstraint, Object> {
    private final TeamInviteService teamInviteService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CreateTeamInviteRequest request = (CreateTeamInviteRequest) value;

        return !teamInviteService.exists(
                request.getTeamId(),
                request.getInvitedIds()
        );
    }
}
