package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.exception.*;
import com.fmi.tournament.organizer.model.*;
import com.fmi.tournament.organizer.repository.ParticipantRepository;
import com.fmi.tournament.organizer.repository.TournamentRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegistrationService {
    private static final String TOURNAMENT_NOT_FOUND_ERROR_MESSAGE = "Tournament with id '%s' does not exist.";
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;

    public RegistrationService(TournamentRepository tournamentRepository, ParticipantRepository participantRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
    }

  public List<Tournament> tournamentForRegistration(UUID participantID){
    Participant participant = this.participantRepository.findById(participantID).orElseThrow();

    return tournamentRepository
        .findAll()
        .stream()
        .filter(tournament -> tournament.getState() == TournamentState.REGISTRATION &&
            !tournament.getParticipants().contains(participant) &&
            tournament.getCategory() == participant.getCategory() &&
            tournament.getSportType() == participant.getSportType() &&
            tournament.getParticipants().size() < tournament.getCapacity())
        .toList();
  }

  public List<Tournament> tournamentForUnregistration(UUID participantID){
    return tournamentRepository
        .findAll()
        .stream()
        .filter(tournament -> tournament.getState() == TournamentState.REGISTRATION && tournament.getParticipants().contains(participantID))
        .toList();
  }

    public void register(UserDetails userDetails, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow(() ->
            new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentID)));

        Participant participant = participantRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
            new RegistrationException("Participant is not created yet."));

        if (tournament.getCapacity() <= tournament.getParticipants().size()){
            throw new RegistrationException("There is enough participants in tournament!");
        }

        if(!(participant.getObjectType().equals("Athlete") && tournament.getTournamentType() == TournamentType.INDIVIDUAL) &&
                !(participant.getObjectType().equals("Team") && tournament.getTournamentType() == TournamentType.TEAM)){
            throw new RegistrationException("The tournament type and participant type do not match!");
        }

        if(tournament.getCategory() != participant.getCategory()){
            throw new RegistrationException("Tournament is for another categorize!");
        }

        if(tournament.getState() != TournamentState.REGISTRATION){
            throw new RegistrationException("The tournament is closed for registration of participants!");
        }

        if(tournament.getSportType() != participant.getSportType()){
            throw new RegistrationException("The tournament is for another sport!");
        }

        if(tournament.getParticipants().contains(participant)){
            throw new RegistrationException("Participant is already registered!");
        }

        tournament.getParticipants().add(participant);

        tournamentRepository.saveAndFlush(tournament);
    }

    public void unregister(UserDetails userDetails, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow(() ->
            new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentID)));

        Participant participant = participantRepository.findByUsername(userDetails.getUsername()).orElseThrow(() ->
            new RegistrationException("Participant is not created yet."));

        if(!tournament.getState().equals(TournamentState.REGISTRATION)){
            throw new RegistrationException("You can only unsubscribe from the tournament while it is in the registration phase!");
        }

        if(!tournament.getParticipants().contains(participant)){
            throw new RegistrationException("There is no such participant registered in the tournament");
        }

        tournament.getParticipants().remove(participant);

        tournamentRepository.saveAndFlush(tournament);
    }
}
