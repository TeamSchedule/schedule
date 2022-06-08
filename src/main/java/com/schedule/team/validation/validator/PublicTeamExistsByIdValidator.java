package com.schedule.team.validation.validator;

import com.schedule.team.service.team.PublicTeamService;
import com.schedule.team.validation.constraint.PublicTeamExistsByIdConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class PublicTeamExistsByIdValidator
        implements ConstraintValidator<PublicTeamExistsByIdConstraint, Long> {
    private final PublicTeamService publicTeamService;

    @Override
    public boolean isValid(Long publicTeamId, ConstraintValidatorContext context) {
        return publicTeamService.existsById(publicTeamId);
    }
}
