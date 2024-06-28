package com.fmi.tournament.organizer.exception;

import java.util.UUID;

public class TournamentNotFoundException extends RuntimeException {
  private static final String ERROR_MESSAGE = "Tournament with id '%s' not found.";

  public TournamentNotFoundException(UUID tournamentId) {
    super(ERROR_MESSAGE.formatted(tournamentId));
  }
}
