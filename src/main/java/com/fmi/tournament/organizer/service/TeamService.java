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
    Team team = new Team(teamDTO.getName(), teamDTO.getSportType(), teamDTO.getYear(), teamDTO.getPlayers(), teamDTO.getManager());
    return teamRepository.save(team);
  }

  public Optional<Team> updateTeamById(UUID id, TeamDTO updatedTeamDetails) {
    Optional<Team> oldTeam = teamRepository.findById(id);
    return oldTeam.flatMap(team -> Optional.of(updateTeamDetails(updatedTeamDetails, team)));
  }

  public void deleteTeamById(UUID id) {
    teamRepository.deleteById(id);
  }

  private Team updateTeamDetails(TeamDTO updatedTeamDetails, Team currentTeam) {
    if (updatedTeamDetails.getName() != null) {
      currentTeam.setName(updatedTeamDetails.getName());
    }
    if (updatedTeamDetails.getSportType() != null) {
      currentTeam.setSportType(updatedTeamDetails.getSportType());
    }
    if (updatedTeamDetails.getYear() != null) {
      currentTeam.setYear(updatedTeamDetails.getYear());
    }
    if (updatedTeamDetails.getPlayers() != null) {
      currentTeam.setPlayers(updatedTeamDetails.getPlayers());
    }
    if (updatedTeamDetails.getManager() != null) {
      currentTeam.setManager(updatedTeamDetails.getManager());
    }
    return teamRepository.save(currentTeam);
  }
}
