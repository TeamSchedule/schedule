package com.schedule.team.validation.validator;

import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.service.team_invite.TeamInviteExistsService;
import com.schedule.team.validation.constraint.UserIsAlreadyInvitedConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UserIsAlreadyInvitedValidator
        implements ConstraintValidator<UserIsAlreadyInvitedConstraint, Object> {
    private final TeamInviteExistsService teamInviteExistsService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CreateTeamInviteRequest request = (CreateTeamInviteRequest) value;

        return !teamInviteExistsService.exists(
                request.getTeamId(),
                request.getInvitedIds()
        );
    }
}
