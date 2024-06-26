package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.exception.*;
import com.fmi.tournament.organizer.model.*;
import com.fmi.tournament.organizer.repository.ParticipantRepository;
import com.fmi.tournament.organizer.repository.TournamentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrationService {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;

    public RegistrationService(TournamentRepository tournamentRepository, ParticipantRepository participantRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
    }

    public void registration(UUID participantID, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();

        if(tournament.getCapacity() <= tournament.getParticipants().size()){
            throw new InvalidTournamentCapacityException("There is enough participants in tournament!");
        }

        Participant participant = participantRepository.findById(participantID).orElseThrow();

        if(!(participant.getObjectType().equals("Athlete") && tournament.getType() == TournamentType.Individual) ||
                !(participant.getObjectType().equals("Team") && tournament.getType() == TournamentType.Team)){
            throw new InvalidTournamentTypeException("The tournament type and participant type do not the same!");
        }

        if(tournament.getCategorize() != participant.getCategorize()){
            throw new InvalidCategorizeException("Tournament is for another categorize!");
        }

        if(tournament.getState() != TournamentState.REGISTRATION){
            throw new InvalidTournamentStateException("The tournament is closed for registration of participants!");
        }

        if(tournament.getSportType() != participant.getSportType()){
            throw new InvalidParticipantSportTypeException("The tournament is for another sport!");
        }

        if(tournament.getParticipants().contains(participant)){
            throw new ParticipantAlreadyRegisteredInTournamentException("Participant is already registered!");
        }

        tournament.getParticipants().add(participant);
        tournamentRepository.saveAndFlush(tournament);
    }

    public void unregistration(UUID participantID, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
        Participant participant = participantRepository.findById(participantID).orElseThrow();

        if(!tournament.getState().equals(TournamentState.REGISTRATION)){
            throw new InvalidTournamentStateException("You can only unsubscribe from the tournament while it is in the registration phase!");
        }

        if(!tournament.getParticipants().contains(participant)){
            throw new ParticipantIsNotRegisteredInTournamentException("There is no such participant registered in the tournament");
        }

        tournament.getParticipants().remove(participant);
        tournamentRepository.saveAndFlush(tournament);
    }
}
