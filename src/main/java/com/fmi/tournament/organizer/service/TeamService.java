package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.TeamCreateDTO;
import com.fmi.tournament.organizer.dto.TeamResponseDTO;
import com.fmi.tournament.organizer.model.Team;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
  private final TeamRepository teamRepository;

  @Autowired
  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  public List<TeamResponseDTO> getAllTeams() {
    return teamRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public Optional<TeamResponseDTO> getTeamById(UUID id) {
    return teamRepository.findById(id).map(this::toResponseDto);
  }

  public TeamResponseDTO createTeam(UserDetails userDetails, TeamCreateDTO teamDTO) {
    Team team = new Team(teamDTO.getName(), userDetails.getUsername(), teamDTO.getSportType(), teamDTO.getEstablishmentYear(), teamDTO.getPlayers(),
        teamDTO.getManager(), teamDTO.getCategory());

    teamRepository.saveAndFlush(team);

    return toResponseDto(team);
  }

  public Optional<TeamResponseDTO> updateTeamById(UUID id, TeamCreateDTO updatedTeam) {
    Optional<Team> currentTeam = teamRepository.findById(id);
    return currentTeam.map(team -> toResponseDto(updateTeamDetails(updatedTeam, team)));
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
