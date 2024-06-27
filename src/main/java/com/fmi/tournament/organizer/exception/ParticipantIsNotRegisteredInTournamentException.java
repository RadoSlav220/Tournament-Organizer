package com.fmi.tournament.organizer.exception;

public class ParticipantIsNotRegisteredInTournamentException extends RuntimeException{
    public ParticipantIsNotRegisteredInTournamentException(String message) {
        super(message);
    }
}
