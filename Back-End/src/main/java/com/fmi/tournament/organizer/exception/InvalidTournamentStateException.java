package com.fmi.tournament.organizer.exception;

public class InvalidTournamentStateException extends RuntimeException{
    public InvalidTournamentStateException(String message) {
        super(message);
    }
}
