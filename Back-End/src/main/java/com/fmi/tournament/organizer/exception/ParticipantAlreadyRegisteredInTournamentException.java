package com.fmi.tournament.organizer.exception;

public class ParticipantAlreadyRegisteredInTournamentException extends RuntimeException {
    public ParticipantAlreadyRegisteredInTournamentException(String message) {
        super(message);
    }
}
