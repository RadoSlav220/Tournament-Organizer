package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentCreateDTO;
import com.fmi.tournament.organizer.dto.KnockOutTournamentResponseDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import com.fmi.tournament.organizer.repository.MatchRepository;
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
  private final MatchRepository matchRepository;

  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository, MatchRepository matchRepository) {
    this.knockOutTournamentRepository = knockOutTournamentRepository;
    this.matchRepository = matchRepository;
  }

  public KnockOutTournamentResponseDTO createKnockOutTournament(KnockOutTournamentCreateDTO knockOutTournamentCreateDTO) {
    if (!isPowerOfTwo(knockOutTournamentCreateDTO.getCapacity())) {
      throw new InvalidTournamentCapacityException("The capacity of a KnockOutTournament must be a power of 2.");
    }

    KnockOutTournament knockOutTournament =
        new KnockOutTournament(knockOutTournamentCreateDTO.getName(), knockOutTournamentCreateDTO.getDescription(),
            knockOutTournamentCreateDTO.getSportType(),
            knockOutTournamentCreateDTO.getCapacity());
    knockOutTournamentRepository.saveAndFlush(knockOutTournament);
    return toResponseDto(knockOutTournament);
  }

  private KnockOutTournamentResponseDTO toResponseDto(KnockOutTournament knockOutTournament) {
    return new KnockOutTournamentResponseDTO(
        knockOutTournament.getId(),
        knockOutTournament.getName(),
        knockOutTournament.getDescription(),
        knockOutTournament.getSportType(),
        knockOutTournament.getState(),
        knockOutTournament.getCapacity(),
        knockOutTournament.getParticipants().stream().map(Participant::getId).toList(),
        knockOutTournament.getMatches().stream().map(Match::getId).toList(),
        knockOutTournament.getAdvancedToNextRoundParticipantsIds(),
        knockOutTournament.getYetToPlayParticipantsIds(),
        knockOutTournament.getKnockedOutParticipantsIds());
  }

  public List<KnockOutTournamentResponseDTO> getAllKnockOutTournaments() {
    return knockOutTournamentRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public Optional<KnockOutTournamentResponseDTO> getKnockOutTournamentById(UUID id) {
    return knockOutTournamentRepository.findById(id).map(this::toResponseDto);
  }

  public Optional<KnockOutTournamentResponseDTO> updateKnockOutTournamentById(UUID id, KnockOutTournamentCreateDTO updatedTournament) {
    Optional<KnockOutTournament> currentTournament = knockOutTournamentRepository.findById(id);
    return currentTournament.map(tournament -> toResponseDto(updateTournamentDetails(updatedTournament, tournament)));
  }

  public void deleteKnockOutTournamentById(UUID id) {
    knockOutTournamentRepository.deleteById(id);
  }

  private KnockOutTournament updateTournamentDetails(KnockOutTournamentCreateDTO updatedTournament, KnockOutTournament currentTournament) {
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

    return knockOutTournamentRepository.saveAndFlush(currentTournament);
  }

  public Optional<KnockOutTournamentResponseDTO> startTournamentById(UUID tournamentId) {
    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
    return foundTournament.map(tournament -> toResponseDto(startTournament(tournament)));
  }

//  public Optional<KnockOutTournament> advanceToNextRoundTournamentById(UUID tournamentId) {
//    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
//    return foundTournament.flatMap(tournament -> Optional.of(advanceToNextRound(tournament)));
//  }

  private KnockOutTournament startTournament(KnockOutTournament knockOutTournament) {
    List<Participant> participants = knockOutTournament.getParticipants();

    if (participants.size() < 2 || !isPowerOfTwo(participants.size())) {
      throw new UnsupportedOperationException("Not appropriate participants count"); // Create a special exception for this
    } else if (knockOutTournament.getState() != TournamentState.NOT_STARTED && knockOutTournament.getState() != TournamentState.REGISTRATION) {
      throw new UnsupportedOperationException("Only not started tournaments can be started"); // Create a special exception for this
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
