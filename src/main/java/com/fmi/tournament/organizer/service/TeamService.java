package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.TeamDTO;
import com.fmi.tournament.organizer.model.Team;
import com.fmi.tournament.organizer.repository.TeamRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {
  private final TeamRepository teamRepository;

  @Autowired
  public TeamService(TeamRepository teamRepository) {
    this.teamRepository = teamRepository;
  }

  public List<Team> getAllTeams() {
    return teamRepository.findAll();
  }

  public Optional<Team> getTeamById(UUID id) {
    return teamRepository.findById(id);
  }

  public Team createTeam(TeamDTO teamDTO) {
    Team team = new Team(teamDTO.getName(), teamDTO.getSportType(), teamDTO.getEstablishmentYear(), teamDTO.getPlayers(), teamDTO.getManager());
    return teamRepository.save(team);
  }

  public Optional<Team> updateTeamById(UUID id, TeamDTO updatedTeam) {
    Optional<Team> currentTeam = teamRepository.findById(id);
    return currentTeam.flatMap(team -> Optional.of(updateTeamDetails(updatedTeam, team)));
  }

  public void deleteTeamById(UUID id) {
    teamRepository.deleteById(id);
  }

  private Team updateTeamDetails(TeamDTO updatedTeam, Team currentTeam) {
    if (updatedTeam.getName() != null) {
      currentTeam.setName(updatedTeam.getName());
    }
    if (updatedTeam.getSportType() != null) {
      currentTeam.setSportType(updatedTeam.getSportType());
    }
    if (updatedTeam.getEstablishmentYear() != null) {
      currentTeam.setYear(updatedTeam.getEstablishmentYear());
    }
    if (updatedTeam.getPlayers() != null) {
      currentTeam.setPlayers(updatedTeam.getPlayers());
    }
    if (updatedTeam.getManager() != null) {
      currentTeam.setManager(updatedTeam.getManager());
    }
    return teamRepository.save(currentTeam);
  }
}
