package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.TeamDTO;
import com.fmi.tournament.organizer.model.Team;
import com.fmi.tournament.organizer.service.TeamService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {
  private final TeamService teamService;

  @Autowired
  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @GetMapping
  public ResponseEntity<List<Team>> getAllTeams() {
    List<Team> teams = teamService.getAllTeams();
    if (teams.isEmpty()) {
      return new ResponseEntity<>(teams, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(teams, HttpStatus.OK);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Team> getTeamById(@PathVariable UUID id) {
    Optional<Team> fetchedTeam = teamService.getTeamById(id);
    return fetchedTeam.map(team -> new ResponseEntity<>(team, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/create")
  public ResponseEntity<Team> createTeam(@RequestBody TeamDTO team) {
    Team newTeam = teamService.createTeam(team);
    return new ResponseEntity<>(newTeam, HttpStatus.OK);
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<Team> updateTeamById(@PathVariable UUID id, @RequestBody TeamDTO newTeam) {
    Optional<Team> updatedTeam = teamService.updateTeamById(id, newTeam);
    return updatedTeam.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<HttpStatus> deleteTeamById(@PathVariable UUID id) {
    teamService.deleteTeamById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
