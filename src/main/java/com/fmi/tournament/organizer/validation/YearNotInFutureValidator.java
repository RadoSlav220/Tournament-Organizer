package com.fmi.tournament.organizer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class YearNotInFutureValidator implements ConstraintValidator<YearNotInFuture, Integer> {
  @Override
  public void initialize(YearNotInFuture constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Integer year, ConstraintValidatorContext constraintValidatorContext) {
    LocalDate now = LocalDate.now();
    return year == null || (year > 0 && year <= now.getYear());
  }
}
