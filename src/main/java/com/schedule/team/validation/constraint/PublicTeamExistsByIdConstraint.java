package com.schedule.team.validation.constraint;

import com.schedule.team.validation.validator.PublicTeamExistsByIdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PublicTeamExistsByIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicTeamExistsByIdConstraint {
    String message() default "Team with specified id is not present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
