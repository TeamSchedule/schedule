package com.schedule.team.validation.validator;

import com.schedule.team.model.request.CreateTeamInviteRequest;
import com.schedule.team.service.team_color.TeamColorExistsService;
import com.schedule.team.validation.constraint.UserIsAlreadyTeamMemberConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UserIsAlreadyTeamMemberValidator
        implements ConstraintValidator<UserIsAlreadyTeamMemberConstraint, Object> {
    private final TeamColorExistsService teamColorExistsService;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CreateTeamInviteRequest request = (CreateTeamInviteRequest) value;

        return !teamColorExistsService.exists(request.getTeamId(), request.getInvitedIds());
    }
}
