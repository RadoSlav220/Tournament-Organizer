package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.model.*;
import com.fmi.tournament.organizer.repository.MatchRepository;
import com.fmi.tournament.organizer.repository.ParticipantRepository;
import com.fmi.tournament.organizer.repository.TournamentRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final ParticipantRepository participantRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public TournamentService(ParticipantRepository participantRepository, TournamentRepository tournamentRepository, MatchRepository matchRepository) {
        this.participantRepository = participantRepository;
        this.tournamentRepository = tournamentRepository;
        this.matchRepository = matchRepository;
    }

    public void registration(UUID participantID, UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();

        if(tournament.getCapacity() <= tournament.getParticipants().size()){
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is enough participants in tournament!");
        }

        Participant participant = participantRepository.findById(participantID).orElseThrow();
        tournament.getParticipants().add(participant);
        tournamentRepository.save(tournament);
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
        tournamentRepository.save(tournament);
    }

    public List<Participant> getParticipants(UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
        return tournament.getParticipants();
    }

    public List<Match> getMatches(UUID tournamentID){
        Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
        return tournament.getMatches();
    }

    public void playMatch(UUID matchID, int resultHomeParticipant, int resultAwayParticipant){
        Match match = matchRepository.findById(matchID).orElseThrow();

        match.setResultHomeParticipant(resultHomeParticipant);
        match.setResultAwayParticipant(resultAwayParticipant);

        //TODO: Change Status and information of participations
        match.getHomeParticipant().getHomeMatches().add(match);
        match.getAwayParticipant().getAwayMatches().add(match);
        match.setState(MatchState.FINISHED);

        matchRepository.save(match);
    }
}
