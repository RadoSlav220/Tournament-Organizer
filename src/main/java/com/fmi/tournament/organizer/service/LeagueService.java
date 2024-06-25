package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.LeagueCreateDTO;
import com.fmi.tournament.organizer.dto.LeagueResponseDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.League;
import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.MatchState;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.repository.LeagueRepository;
import com.fmi.tournament.organizer.repository.MatchRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LeagueService {
  private final LeagueRepository leagueRepository;
  private final MatchRepository matchRepository;
  private final MatchScheduler matchScheduler;

  @Autowired
  public LeagueService(LeagueRepository leagueRepository, MatchRepository matchRepository, MatchScheduler matchScheduler) {
    this.leagueRepository = leagueRepository;
    this.matchRepository = matchRepository;
    this.matchScheduler = matchScheduler;
  }

  public LeagueResponseDTO createLeague(LeagueCreateDTO leagueCreateDTO) {
    League league =
        new League(leagueCreateDTO.getName(), leagueCreateDTO.getDescription(),
            leagueCreateDTO.getSportType(),
            leagueCreateDTO.getCapacity());
    leagueRepository.saveAndFlush(league);
    return toResponseDto(league);
  }

  public List<LeagueResponseDTO> getAllLeagues() {
    return leagueRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public Optional<LeagueResponseDTO> getLeagueById(UUID leagueId) {
    return leagueRepository.findById(leagueId).map(this::toResponseDto);
  }

  public Optional<LeagueResponseDTO> updateLeagueById(UUID leagueId, LeagueCreateDTO updatedLeague) {
    Optional<League> currentLeague = leagueRepository.findById(leagueId);
    return currentLeague.map(league -> toResponseDto(updateLeagueDetails(updatedLeague, league)));
  }

  public void deleteLeagueById(UUID id) {
    leagueRepository.deleteById(id);
  }

  public Optional<LeagueResponseDTO> startLeagueById(UUID leagueId) {
    Optional<League> foundLeague = leagueRepository.findById(leagueId);
    return foundLeague.map(league -> toResponseDto(startLeague(league)));
  }

  public Optional<LeagueResponseDTO> playMatchById(UUID leagueId, UUID matchId, int homeScore, int awayScore) {
    Optional<League> foundLeague = leagueRepository.findById(leagueId);
    return foundLeague.map(league -> toResponseDto(playMatch(league, matchId, homeScore, awayScore)));
  }

  private LeagueResponseDTO toResponseDto(League league) {
    return new LeagueResponseDTO(
        league.getId(),
        league.getName(),
        league.getDescription(),
        league.getSportType(),
        league.getState(),
        league.getCapacity(),
        league.getParticipants().stream().map(Participant::getId).toList(),
        league.getMatches().stream().map(Match::getId).toList(),
        league.getResults());
  }

  private League updateLeagueDetails(LeagueCreateDTO updatedLeague, League currentLeague) {
    if (updatedLeague.getCapacity() != null && currentLeague.getParticipants().size() > updatedLeague.getCapacity()) {
      throw new InvalidTournamentCapacityException("The updated capacity cannot be less than the number of participants already enrolled.");
    }

    if (updatedLeague.getName() != null) {
      currentLeague.setName(updatedLeague.getName());
    }
    if (updatedLeague.getDescription() != null) {
      currentLeague.setDescription(updatedLeague.getDescription());
    }
    if (updatedLeague.getSportType() != null) {
      currentLeague.setSportType(updatedLeague.getSportType());
    }
    if (updatedLeague.getCapacity() != null) {
      currentLeague.setCapacity(updatedLeague.getCapacity());
    }

    return leagueRepository.saveAndFlush(currentLeague);
  }

  private League playMatch(League league, UUID matchId, int homeScore, int awayScore) {
    Optional<Match> maybeMatch = matchRepository.findById(matchId);
    if (maybeMatch.isEmpty()) {
      throw new UnsupportedOperationException("Match does not exist"); // create a special exception for this
    }

    Match match = maybeMatch.get();
    if (match.getState() == MatchState.FINISHED) {
      throw new IllegalArgumentException("This match is already finished."); // create a special exception for this
    }

    if (!league.getMatches().contains(match)) {
      throw new IllegalArgumentException("This match is not part of this tournament"); // create a special exception for this
    }

    return processMatchScore(league, match, homeScore, awayScore);
  }

  private League processMatchScore(League league, Match match, int homeScore, int awayScore) {
    UUID homeParticipantId = match.getHomeParticipantID();
    UUID awayParticipantId = match.getAwayParticipantID();

    match.setHomeResult(homeScore);
    match.setAwayResult(awayScore);
    match.setState(MatchState.FINISHED);

    Integer currentTotalHomeResult = league.getResults().get(homeParticipantId);
    Integer currentTotalAwayResult = league.getResults().get(awayParticipantId);

    if (homeScore > awayScore) {
      league.getResults().put(homeParticipantId, currentTotalHomeResult + 3);
    } else if (homeScore == awayScore) {
      league.getResults().put(homeParticipantId, currentTotalHomeResult + 1);
      league.getResults().put(awayParticipantId, currentTotalAwayResult + 1);
    } else {
      league.getResults().put(awayParticipantId, currentTotalAwayResult + 3);
    }

    if (isLeagueFinished(league)) {
      league.setState(TournamentState.FINISHED);
    }

    matchRepository.save(match);
    leagueRepository.saveAndFlush(league);

    return league;
  }

  private boolean isLeagueFinished(League league) {
    return league.getMatches().stream().allMatch(match -> match.getState() == MatchState.FINISHED);
  }

  private League startLeague(League league) {
    List<Participant> participants = league.getParticipants();

    if (participants.size() < 2) {
      throw new UnsupportedOperationException("Not appropriate participants count"); // Create a special exception for this
    } else if (league.getState() != TournamentState.REGISTRATION) {
      throw new UnsupportedOperationException("Only not started tournaments can be started"); // Create a special exception for this
    }

    participants.forEach(participant -> league.getResults().put(participant.getId(), 0));

    matchScheduler.scheduleLeagueGames(league);
    league.setState(TournamentState.ONGOING);
    leagueRepository.saveAndFlush(league);

    return league;
  }
}
