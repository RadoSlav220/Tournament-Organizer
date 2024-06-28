package com.fmi.tournament.organizer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { YearNotInFutureValidator.class })
public @interface YearNotInFuture {
  String message() default "Year cannot be in the future.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
