package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentCreateDTO;
import com.fmi.tournament.organizer.dto.KnockOutTournamentResponseDTO;
import com.fmi.tournament.organizer.exception.ForbiddenActionException;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.MatchState;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import com.fmi.tournament.organizer.repository.MatchRepository;
import com.fmi.tournament.organizer.security.TournamentAccessChecker;
import com.fmi.tournament.organizer.security.model.Permission;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class KnockOutTournamentService {
  private static final String FORBIDDEN_ACTION_ERROR_MESSAGE =
      "Knock Out Tournament with id '%s' exists but you have no permissions to execute the requested action.";
  private static final String TOURNAMENT_NOT_FOUND_ERROR_MESSAGE = "Knock Out Tournament with id '%s' does not exist.";
  private final KnockOutTournamentRepository knockOutTournamentRepository;
  private final MatchRepository matchRepository;
  private final MatchScheduler matchScheduler;
  private final TournamentAccessChecker tournamentAccessChecker;

  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository, MatchRepository matchRepository,
                                   MatchScheduler matchScheduler, TournamentAccessChecker tournamentAccessChecker) {
    this.knockOutTournamentRepository = knockOutTournamentRepository;
    this.matchRepository = matchRepository;
    this.matchScheduler = matchScheduler;
    this.tournamentAccessChecker = tournamentAccessChecker;
  }

  public KnockOutTournamentResponseDTO createKnockOutTournament(UserDetails userDetails, KnockOutTournamentCreateDTO knockOutTournamentCreateDTO) {
    if (!isPowerOfTwo(knockOutTournamentCreateDTO.getCapacity())) {
      throw new InvalidTournamentCapacityException("The capacity of a KnockOutTournament must be a power of 2.");
    }

    KnockOutTournament knockOutTournament =
        new KnockOutTournament(knockOutTournamentCreateDTO.getName(), knockOutTournamentCreateDTO.getDescription(),
            knockOutTournamentCreateDTO.getSportType(), knockOutTournamentCreateDTO.getTournamentType(), knockOutTournamentCreateDTO.getCategory(),
            knockOutTournamentCreateDTO.getCapacity(), userDetails.getUsername());

    knockOutTournamentRepository.saveAndFlush(knockOutTournament);

    return toResponseDto(knockOutTournament);
  }

  public List<KnockOutTournamentResponseDTO> getAllKnockOutTournaments(UserDetails userDetails) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    if (authorities.contains(new SimpleGrantedAuthority(Permission.READ_ANY_TOURNAMENT.toString()))) {
      return knockOutTournamentRepository.findAll().stream()
          .map(this::toResponseDto)
          .toList();
    }

    if (authorities.contains(new SimpleGrantedAuthority(Permission.READ_OWNED_TOURNAMENT.toString()))) {
      return knockOutTournamentRepository.findByOrganizer(userDetails.getUsername()).stream()
          .map(this::toResponseDto)
          .toList();
    }

    return Collections.emptyList();
  }

  public KnockOutTournamentResponseDTO getKnockOutTournamentById(UserDetails userDetails, UUID tournamentId) {
    KnockOutTournament foundTournament = knockOutTournamentRepository.findById(tournamentId)
        .orElseThrow(() -> new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentId)));

    if (!tournamentAccessChecker.isTournamentAccessibleForReading(userDetails, foundTournament)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(tournamentId));
    }

    return toResponseDto(foundTournament);
  }

  public KnockOutTournamentResponseDTO updateKnockOutTournamentById(UserDetails userDetails, UUID tournamentId,
                                                                    KnockOutTournamentCreateDTO updatedTournament) {
    KnockOutTournament currentTournament = knockOutTournamentRepository.findById(tournamentId)
        .orElseThrow(() -> new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentId)));

    if (!tournamentAccessChecker.isTournamentAccessibleForModification(userDetails, currentTournament)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(tournamentId));
    }

    return toResponseDto(updateTournamentDetails(updatedTournament, currentTournament));
  }

  public void deleteKnockOutTournamentById(UserDetails userDetails, UUID tournamentId) {
    KnockOutTournament foundTournament =
        knockOutTournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentId)));

    if (tournamentAccessChecker.isTournamentAccessibleForDeletion(userDetails, foundTournament)) {
      knockOutTournamentRepository.deleteById(tournamentId);
    } else {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(tournamentId));
    }

    knockOutTournamentRepository.deleteById(tournamentId);
  }

  public KnockOutTournamentResponseDTO startTournamentById(UserDetails userDetails, UUID tournamentId) {
    KnockOutTournament foundTournament =
        knockOutTournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentId)));

    if (!tournamentAccessChecker.isTournamentAccessibleForModification(userDetails, foundTournament)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(tournamentId));
    }

    return toResponseDto(startTournament(foundTournament));
  }

  public KnockOutTournamentResponseDTO playMatchById(UserDetails userDetails, UUID tournamentId, UUID matchId, int homeScore, int awayScore) {
    KnockOutTournament foundTournament =
        knockOutTournamentRepository.findById(tournamentId)
            .orElseThrow(() -> new NoSuchElementException(TOURNAMENT_NOT_FOUND_ERROR_MESSAGE.formatted(tournamentId)));

    if (!tournamentAccessChecker.isTournamentAccessibleForModification(userDetails, foundTournament)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(tournamentId));
    }

    return toResponseDto(playMatch(foundTournament, matchId, homeScore, awayScore));
  }

  private KnockOutTournamentResponseDTO toResponseDto(KnockOutTournament knockOutTournament) {
    return new KnockOutTournamentResponseDTO(
        knockOutTournament.getId(),
        knockOutTournament.getName(),
        knockOutTournament.getDescription(),
        knockOutTournament.getSportType(),
        knockOutTournament.getCategory(),
        knockOutTournament.getState(),
        knockOutTournament.getTournamentType(),
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
