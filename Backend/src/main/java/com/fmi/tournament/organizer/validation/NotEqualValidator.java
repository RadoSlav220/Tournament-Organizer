package com.fmi.tournament.organizer.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEqualValidator implements ConstraintValidator<NotEqual, Enum<?>> {

  private Enum<?> forbiddenValue;

  @Override
  public void initialize(NotEqual constraintAnnotation) {
    Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
    this.forbiddenValue = Enum.valueOf(enumClass.asSubclass(Enum.class), constraintAnnotation.forbiddenValue());
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    if (value == null) {
      return true; // You might want to handle null values differently
    }
    return !value.equals(forbiddenValue);
  }
}
