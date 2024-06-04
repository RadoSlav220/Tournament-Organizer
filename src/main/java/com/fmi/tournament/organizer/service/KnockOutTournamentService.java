package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.*;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import com.fmi.tournament.organizer.repository.MatchRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnockOutTournamentService {
  private final KnockOutTournamentRepository knockOutTournamentRepository;
  private final MatchRepository matchRepository;
  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository, MatchRepository matchRepository) {
      this.knockOutTournamentRepository = knockOutTournamentRepository;
      this.matchRepository = matchRepository;
  }

  public KnockOutTournament createKnockOutTournament(KnockOutTournamentDTO knockOutTournamentDTO) {
    if (!isPowerOfTwo(knockOutTournamentDTO.getCapacity())) {
      throw new InvalidTournamentCapacityException("The capacity of a KnockOutTournament must be a power of 2.");
    }

    KnockOutTournament knockOutTournament =
        new KnockOutTournament(knockOutTournamentDTO.getName(), knockOutTournamentDTO.getDescription(), knockOutTournamentDTO.getSportType(),
            knockOutTournamentDTO.getCapacity());
    knockOutTournamentRepository.saveAndFlush(knockOutTournament);
    return knockOutTournament;
  }

  public List<KnockOutTournament> getAllKnockOutTournaments() {
    return knockOutTournamentRepository.findAll();
  }

  public Optional<KnockOutTournament> getKnockOutTournamentById(UUID id) {
    return knockOutTournamentRepository.findById(id);
  }

  public Optional<KnockOutTournament> updateKnockOutTournamentById(UUID id, KnockOutTournamentDTO updatedTournament) {
    Optional<KnockOutTournament> currentTournament = knockOutTournamentRepository.findById(id);
    return currentTournament.flatMap(tournament -> Optional.of(updateTournamentDetails(updatedTournament, tournament)));
  }

  public void deleteKnockOutTournamentById(UUID id) {
    knockOutTournamentRepository.deleteById(id);
  }

  private KnockOutTournament updateTournamentDetails(KnockOutTournamentDTO updatedTournament, KnockOutTournament currentTournament) {
    if (updatedTournament.getCapacity() != null && currentTournament.getParticipants().size() > updatedTournament.getCapacity()) {
      throw new InvalidTournamentCapacityException("The updated capacity cannot be less than the number of participants already enrolled.");
    } else if (updatedTournament.getCapacity() != null && !isPowerOfTwo(updatedTournament.getCapacity())) {
      throw new InvalidTournamentCapacityException("The updated capacity must be a power of two.");
    }

    if (updatedTournament.getName() != null) {
      currentTournament.setName(updatedTournament.getName());
    }
    if (updatedTournament.getDescription() != null) {
      currentTournament.setDescription(updatedTournament.getDescription());
    }
    if (updatedTournament.getSportType() != null) {
      currentTournament.setSportType(updatedTournament.getSportType());
    }
    if (updatedTournament.getCapacity() != null) {
      currentTournament.setCapacity(updatedTournament.getCapacity());
    }

    return knockOutTournamentRepository.save(currentTournament);
  }

  public void startTournament(UUID id){
      KnockOutTournament tournament = knockOutTournamentRepository.findById(id).orElseThrow();
      tournament.setState(TournamentState.ONGOING);
      //TODO:To randomize a list



      for(int i = 0; i < tournament.getParticipants().size() - 1; i+=2){
          Match newMatch = new Match (LocalDate.of(2024, 6, 27), tournament, tournament.getParticipants().get(i),
                  tournament.getParticipants().get(i + 1), MatchState.NOT_STARTED, 0, 0);

          matchRepository.save(newMatch);
      }

      knockOutTournamentRepository.save(tournament);
  }

  private boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }
}
