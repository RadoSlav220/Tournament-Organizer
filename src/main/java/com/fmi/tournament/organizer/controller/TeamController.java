package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.TeamCreateDTO;
import com.fmi.tournament.organizer.dto.TeamResponseDTO;
import com.fmi.tournament.organizer.service.TeamService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/teams")
public class TeamController {
  private final TeamService teamService;

  @Autowired
  public TeamController(TeamService teamService) {
    this.teamService = teamService;
  }

  @PreAuthorize("hasAuthority('CREATE_PARTICIPANT')")
  @PostMapping
  public ResponseEntity<TeamResponseDTO> createTeam(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody @Valid TeamCreateDTO team) {
    TeamResponseDTO newTeam = teamService.createTeam(userDetails, team);
    return new ResponseEntity<>(newTeam, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<TeamResponseDTO>> getAllTeams() {
    List<TeamResponseDTO> teams = teamService.getAllTeams();
    return new ResponseEntity<>(teams, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TeamResponseDTO> getTeamById(@PathVariable UUID id) {
    Optional<TeamResponseDTO> fetchedTeam = teamService.getTeamById(id);
    return fetchedTeam.map(team -> new ResponseEntity<>(team, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TeamResponseDTO> updateTeamById(@PathVariable UUID id, @RequestBody @Valid TeamCreateDTO updatedTeam) {
    Optional<TeamResponseDTO> resultTeam = teamService.updateTeamById(id, updatedTeam);
    return resultTeam.map(team -> new ResponseEntity<>(team, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTeamById(@PathVariable UUID id) {
    teamService.deleteTeamById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}