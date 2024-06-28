package com.fmi.tournament.organizer.exception;

import java.util.UUID;

public class TournamentForbiddenException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Tournament with id '%s' exists but you have no permissions to execute the current request.";

  public TournamentForbiddenException(UUID tournamentId) {
    super(ERROR_MESSAGE.formatted(tournamentId));
  }
}
