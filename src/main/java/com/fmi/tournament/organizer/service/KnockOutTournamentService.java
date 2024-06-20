package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.SportType;
import com.fmi.tournament.organizer.model.Team;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.AthleteRepository;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnockOutTournamentService {
  private final KnockOutTournamentRepository knockOutTournamentRepository;
  @Autowired
  private AthleteRepository athleteRepository;

  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository) {
    this.knockOutTournamentRepository = knockOutTournamentRepository;
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

  public Optional<KnockOutTournament> startTournamentById(UUID tournamentId) {
    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
    return foundTournament.flatMap(tournament -> Optional.of(startTournament(tournament)));
  }

//  public Optional<KnockOutTournament> advanceToNextRoundTournamentById(UUID tournamentId) {
//    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
//    return foundTournament.flatMap(tournament -> Optional.of(advanceToNextRound(tournament)));
//  }

  private KnockOutTournament startTournament(KnockOutTournament knockOutTournament) {
    List<Athlete> athletes = athleteRepository.findAll();

    Athlete a1 = athletes.get(0);
    Athlete a2 = athletes.get(1);

    knockOutTournament.getParticipants().add(a1);
    knockOutTournament.getParticipants().add(a2);

    // --------------------------------------------------------------------------------
    List<Participant> participants = knockOutTournament.getParticipants();

    if (participants.size() < 2 || !isPowerOfTwo(participants.size())) {
      throw new UnsupportedOperationException("Not appropriate participants count"); // Create a special exception for this
    } else if (knockOutTournament.getState() != TournamentState.NOT_STARTED && knockOutTournament.getState() != TournamentState.REGISTRATION) {
      throw new UnsupportedOperationException("Only not started tournaments can be started");
    }

    List<UUID> yetToPlayParticipantsIds = knockOutTournament.getParticipants().stream().map(Participant::getId).collect(Collectors.toList());

    Collections.shuffle(yetToPlayParticipantsIds);
    knockOutTournament.setYetToPlayParticipantsIds(yetToPlayParticipantsIds);

    knockOutTournament.setState(TournamentState.ONGOING);
    knockOutTournamentRepository.saveAndFlush(knockOutTournament);

    return knockOutTournament;
  }

//  private KnockOutTournament advanceToNextRound(KnockOutTournament knockOutTournament) {
//    if (!knockOutTournament.getYetToPlayParticipants().isEmpty()) {
//      throw new RoundNotFinishedException("Current round has not finished yet. Cannot advance to next round.");
//    }
//
//    knockOutTournament.getYetToPlayParticipants().addAll(knockOutTournament.getAdvancedToNextRoundParticipants());
//    knockOutTournament.getAdvancedToNextRoundParticipants().clear();
//
//    for (int i = 0; i < knockOutTournament.getYetToPlayParticipants().size() - 1; i += 2) {
//      Participant participant1 = knockOutTournament.getYetToPlayParticipants().get(i);
//      Participant participant2 = knockOutTournament.getYetToPlayParticipants().get(i + 1);
//      Match match = new Match(LocalDate.now(), knockOutTournament, participant1, participant2);
//      knockOutTournament.getMatches().add(match);
//    }
//
//    return knockOutTournament;
//  }


  private boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }
}
