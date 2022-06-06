package com.schedule.team.validation.constraint;

import com.schedule.team.validation.validator.UserIsAlreadyInvitedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserIsAlreadyInvitedValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIsAlreadyInvitedConstraint {
    String message() default "User is already invited to this team";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
