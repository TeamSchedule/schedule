package com.schedule.team.validation.constraint;

import com.schedule.team.validation.validator.UserIsAlreadyTeamMemberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserIsAlreadyTeamMemberValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIsAlreadyTeamMemberConstraint {
    String message() default "User is already a team member";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
