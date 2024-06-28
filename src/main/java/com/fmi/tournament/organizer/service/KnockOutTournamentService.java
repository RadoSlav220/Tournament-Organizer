package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentCreateDTO;
import com.fmi.tournament.organizer.dto.KnockOutTournamentResponseDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.MatchState;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import com.fmi.tournament.organizer.repository.MatchRepository;
import com.fmi.tournament.organizer.security.AuthenticatedUserUtil;
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
  private final MatchScheduler matchScheduler;

  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository, MatchRepository matchRepository,
                                   MatchScheduler matchScheduler) {
    this.knockOutTournamentRepository = knockOutTournamentRepository;
    this.matchRepository = matchRepository;
    this.matchScheduler = matchScheduler;
  }

  public KnockOutTournamentResponseDTO createKnockOutTournament(KnockOutTournamentCreateDTO knockOutTournamentCreateDTO) {
    if (!isPowerOfTwo(knockOutTournamentCreateDTO.getCapacity())) {
      throw new InvalidTournamentCapacityException("The capacity of a KnockOutTournament must be a power of 2.");
    }

    KnockOutTournament knockOutTournament =
        new KnockOutTournament(knockOutTournamentCreateDTO.getName(), knockOutTournamentCreateDTO.getDescription(),
            knockOutTournamentCreateDTO.getSportType(), knockOutTournamentCreateDTO.getCapacity(), AuthenticatedUserUtil.getCurrentUsername());
    knockOutTournamentRepository.saveAndFlush(knockOutTournament);
    return toResponseDto(knockOutTournament);
  }

  public List<KnockOutTournamentResponseDTO> getAllKnockOutTournaments() {
    return knockOutTournamentRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public Optional<KnockOutTournamentResponseDTO> getKnockOutTournamentById(UUID tournamentId) {
    return knockOutTournamentRepository.findById(tournamentId).map(this::toResponseDto);
  }

  public Optional<KnockOutTournamentResponseDTO> updateKnockOutTournamentById(UUID tournamentId, KnockOutTournamentCreateDTO updatedTournament) {
    Optional<KnockOutTournament> currentTournament = knockOutTournamentRepository.findById(tournamentId);
    return currentTournament.map(tournament -> toResponseDto(updateTournamentDetails(updatedTournament, tournament)));
  }

  public void deleteKnockOutTournamentById(UUID id) {
    knockOutTournamentRepository.deleteById(id);
  }

  public Optional<KnockOutTournamentResponseDTO> startTournamentById(UUID tournamentId) {
    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
    return foundTournament.map(tournament -> toResponseDto(startTournament(tournament)));
  }

  public Optional<KnockOutTournamentResponseDTO> playMatchById(UUID tournamentId, UUID matchId, int homeScore, int awayScore) {
    Optional<KnockOutTournament> foundTournament = knockOutTournamentRepository.findById(tournamentId);
    return foundTournament.map(tournament -> toResponseDto(playMatch(tournament, matchId, homeScore, awayScore)));
  }

  private KnockOutTournamentResponseDTO toResponseDto(KnockOutTournament knockOutTournament) {
    return new KnockOutTournamentResponseDTO(
        knockOutTournament.getId(),
        knockOutTournament.getName(),
        knockOutTournament.getDescription(),
        knockOutTournament.getSportType(),
        knockOutTournament.getState(),
        knockOutTournament.getCapacity(),
        knockOutTournament.getOrganizer(),
        knockOutTournament.getParticipants().stream().map(Participant::getId).toList(),
        knockOutTournament.getMatches().stream().map(Match::getId).toList(),
        knockOutTournament.getAdvancedToNextRoundParticipantsIds(),
        knockOutTournament.getYetToPlayParticipantsIds(),
        knockOutTournament.getKnockedOutParticipantsIds());
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

  private KnockOutTournament playMatch(KnockOutTournament tournament, UUID matchId, int homeScore, int awayScore) {
    if (homeScore == awayScore) {
      throw new IllegalArgumentException("Tie matches are not allowed in a Knock Out Tournament.");
    }

    Optional<Match> maybeMatch = matchRepository.findById(matchId);
    if (maybeMatch.isEmpty()) {
      throw new UnsupportedOperationException("Match does not exist"); // create a special exception for this
    }

    Match match = maybeMatch.get();
    if (match.getState() == MatchState.FINISHED) {
      throw new IllegalArgumentException("This match is already finished."); // create a special exception for this
    }

    if (!tournament.getMatches().contains(match)) {
      throw new IllegalArgumentException("This match is not part of this tournament"); // create a special exception for this
    }

    return processMatchScore(tournament, match, homeScore, awayScore);
  }

  private KnockOutTournament processMatchScore(KnockOutTournament tournament, Match match, int homeScore, int awayScore) {
    UUID homeParticipantId = match.getHomeParticipantID();
    UUID awayParticipantId = match.getAwayParticipantID();

    match.setHomeResult(homeScore);
    match.setAwayResult(awayScore);
    match.setState(MatchState.FINISHED);

    tournament.getYetToPlayParticipantsIds().remove(homeParticipantId);
    tournament.getYetToPlayParticipantsIds().remove(awayParticipantId);

    if (homeScore > awayScore) {
      tournament.getAdvancedToNextRoundParticipantsIds().add(homeParticipantId);
      tournament.getKnockedOutParticipantsIds().add(awayParticipantId);
    } else {
      tournament.getAdvancedToNextRoundParticipantsIds().add(awayParticipantId);
      tournament.getKnockedOutParticipantsIds().add(homeParticipantId);
    }

    if (isRoundOver(tournament)) {
      advanceTournamentToNextRound(tournament);
    }

    matchRepository.save(match);
    knockOutTournamentRepository.saveAndFlush(tournament);

    return tournament;
  }

  private boolean isRoundOver(KnockOutTournament tournament) {
    return tournament.getYetToPlayParticipantsIds().isEmpty();
  }

  private void advanceTournamentToNextRound(KnockOutTournament tournament) {
    if (tournament.getAdvancedToNextRoundParticipantsIds().size() == 1) {
      tournament.setState(TournamentState.FINISHED);
    } else {
      tournament.setYetToPlayParticipantsIds(tournament.getAdvancedToNextRoundParticipantsIds());
      tournament.setAdvancedToNextRoundParticipantsIds(Collections.emptyList());
      matchScheduler.scheduleKnockOutTournamentGames(tournament, tournament.getYetToPlayParticipantsIds());
    }
  }

  private KnockOutTournament startTournament(KnockOutTournament tournament) {
    List<Participant> participants = tournament.getParticipants();

    if (participants.size() < 2 || !isPowerOfTwo(participants.size())) {
      throw new UnsupportedOperationException("Not appropriate participants count"); // Create a special exception for this
    } else if (tournament.getState() != TournamentState.REGISTRATION) {
      throw new UnsupportedOperationException("Only not started tournaments can be started"); // Create a special exception for this
    }

    List<UUID> yetToPlayParticipantsIds = tournament.getParticipants().stream().map(Participant::getId).collect(Collectors.toList());

    Collections.shuffle(yetToPlayParticipantsIds);
    tournament.setYetToPlayParticipantsIds(yetToPlayParticipantsIds);

    matchScheduler.scheduleKnockOutTournamentGames(tournament, yetToPlayParticipantsIds);
    tournament.setState(TournamentState.ONGOING);
    knockOutTournamentRepository.saveAndFlush(tournament);

    return tournament;
  }

  private boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }
}
