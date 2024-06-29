package com.fmi.tournament.organizer.exception;

public class ForbiddenActionException extends RuntimeException {
  public ForbiddenActionException(String message) {
    super(message);
  }
}
