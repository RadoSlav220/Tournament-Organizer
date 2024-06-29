package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.TeamCreateDTO;
import com.fmi.tournament.organizer.dto.TeamResponseDTO;
import com.fmi.tournament.organizer.exception.ForbiddenActionException;
import com.fmi.tournament.organizer.model.Team;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.repository.TeamRepository;
import com.fmi.tournament.organizer.security.ParticipantAccessChecker;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
  private static final String FORBIDDEN_ACTION_ERROR_MESSAGE =
      "Team with id '%s' exists but you have no permissions to execute the requested action.";
  private static final String TEAM_NOT_FOUND_ERROR_MESSAGE = "Team with id '%s' does not exist.";
  private final TeamRepository teamRepository;
  private final ParticipantAccessChecker participantAccessChecker;

  @Autowired
  public TeamService(TeamRepository teamRepository, ParticipantAccessChecker participantAccessChecker) {
    this.teamRepository = teamRepository;
    this.participantAccessChecker = participantAccessChecker;
  }

  public TeamResponseDTO createTeam(UserDetails userDetails, TeamCreateDTO teamDTO) {
    Team team = new Team(teamDTO.getName(), userDetails.getUsername(), teamDTO.getSportType(), teamDTO.getEstablishmentYear(), teamDTO.getPlayers(),
        teamDTO.getManager(), teamDTO.getCategory());

    teamRepository.saveAndFlush(team);

    return toResponseDto(team);
  }

  public List<TeamResponseDTO> getAllTeams() {
    return teamRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public TeamResponseDTO getTeamById(UUID teamId) {
    Team team = teamRepository.findById(teamId).orElseThrow(() -> new NoSuchElementException(TEAM_NOT_FOUND_ERROR_MESSAGE.formatted(teamId)));
    return toResponseDto(team);
  }

  public TeamResponseDTO updateTeamById(UserDetails userDetails, UUID teamId, TeamCreateDTO updatedTeam) {
    Team currentTeam =
        teamRepository.findById(teamId).orElseThrow(() -> new NoSuchElementException(TEAM_NOT_FOUND_ERROR_MESSAGE.formatted(teamId)));

    if (!participantAccessChecker.isParticipantAccessibleForModification(userDetails, currentTeam)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(teamId));
    }

    return toResponseDto(updateTeamDetails(updatedTeam, currentTeam));
  }

  public void deleteTeamById(UUID id) {
    teamRepository.deleteById(id);
  }

  private Team updateTeamDetails(TeamCreateDTO updatedTeam, Team currentTeam) {
    if (updatedTeam.getName() != null) {
      currentTeam.setName(updatedTeam.getName());
    }

    if (updatedTeam.getSportType() != null) {
      currentTeam.setSportType(updatedTeam.getSportType());
    }

    if (updatedTeam.getEstablishmentYear() != null) {
      currentTeam.setEstablishmentYear(updatedTeam.getEstablishmentYear());
    }

    if (updatedTeam.getPlayers() != null) {
      currentTeam.setPlayers(updatedTeam.getPlayers());
    }

    if (updatedTeam.getManager() != null) {
      currentTeam.setManager(updatedTeam.getManager());
    }

    return teamRepository.saveAndFlush(currentTeam);
  }

  private TeamResponseDTO toResponseDto(Team team) {
    return new TeamResponseDTO(team.getId(),
        team.getName(),
        team.getUsername(),
        team.getSportType(),
        team.getCategory(),
        team.getTournaments().stream().map(Tournament::getId).toList(),
        team.getEstablishmentYear(),
        team.getPlayers(),
        team.getManager());
  }
}
