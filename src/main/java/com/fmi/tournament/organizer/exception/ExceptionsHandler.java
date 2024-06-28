package com.fmi.tournament.organizer.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
  @ExceptionHandler(InvalidTournamentCapacityException.class)
  public ResponseEntity<String> handleCapacityValidationException(InvalidTournamentCapacityException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleCapacityValidationException(MethodArgumentNotValidException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unique constraint violation: " + e.getMessage());
  }

  @ExceptionHandler(TournamentNotFoundException.class)
  public ResponseEntity<String> handleTournamentNotFoundException(TournamentNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
  }

  @ExceptionHandler(TournamentForbiddenException.class)
  public ResponseEntity<String> handleTournamentForbiddenException(TournamentForbiddenException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
  }
}
