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

  public Optional<Team> updateTeamById(UUID id, TeamDTO newTeam) {
    Optional<Team> oldTeam = teamRepository.findById(id);

    if (oldTeam.isPresent()) {
      Team updateTeam = oldTeam.get();

      updateTeam.setName(newTeam.getName());
      updateTeam.setSportType(newTeam.getSportType());
      updateTeam.setYear(newTeam.getYear());
      updateTeam.setPlayers(newTeam.getPlayers());
      updateTeam.setManager(newTeam.getManager());

      return Optional.of(teamRepository.save(updateTeam));
    } else {
      return Optional.empty();
    }
  }

  public void deleteTeamById(UUID id) {
    teamRepository.deleteById(id);
  }

}
