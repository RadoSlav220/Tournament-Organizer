package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.ParticipantRepository;
import com.fmi.tournament.organizer.repository.TournamentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ParticipantService {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;

    public ParticipantService(TournamentRepository tournamentRepository, ParticipantRepository participantRepository) {
        this.tournamentRepository = tournamentRepository;
        this.participantRepository = participantRepository;
    }

    public void registration(UUID participantID, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();

        if(tournament.getCapacity() <= tournament.getParticipants().size()){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is enough participants in tournament!");
        }

        Participant participant = participantRepository.findById(participantID).orElseThrow();
        tournament.getParticipants().add(participant);
        tournamentRepository.saveAndFlush(tournament);
    }

    public void unregistration(UUID participantID, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
        Participant participant = participantRepository.findById(participantID).orElseThrow();

        if(!tournament.getState().equals(TournamentState.REGISTRATION)){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can only unsubscribe from the tournament while it is in the registration phase!");
        }

        if(!tournament.getParticipants().contains(participant)){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is no such participant registered in the tournament");
        }

        tournament.getParticipants().remove(participant);
        tournamentRepository.saveAndFlush(tournament);
    }
}
