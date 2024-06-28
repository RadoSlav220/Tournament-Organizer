package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.repository.MatchRepository;
import com.fmi.tournament.organizer.repository.ParticipantRepository;
import com.fmi.tournament.organizer.repository.TournamentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {
  private final TournamentRepository tournamentRepository;
  private final MatchRepository matchRepository;

  @Autowired
  public TournamentService(ParticipantRepository participantRepository, TournamentRepository tournamentRepository, MatchRepository matchRepository) {
    this.tournamentRepository = tournamentRepository;
    this.matchRepository = matchRepository;
  }

  public List<Participant> getParticipants(UUID tournamentID) {
    Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
    return tournament.getParticipants();
  }

  public List<Match> getMatches(UUID tournamentID) {
    Tournament tournament = tournamentRepository.findById(tournamentID).orElseThrow();
    return tournament.getMatches();
  }
}