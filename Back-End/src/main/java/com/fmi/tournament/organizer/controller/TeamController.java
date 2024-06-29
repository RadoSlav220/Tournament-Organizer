package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.TeamCreateDTO;
import com.fmi.tournament.organizer.dto.TeamResponseDTO;
import com.fmi.tournament.organizer.service.TeamService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:4200")
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

  @PreAuthorize("hasAuthority('READ_PARTICIPANT')")
  @GetMapping
  public List<TeamResponseDTO> getAllTeams() {
    return teamService.getAllTeams();
  }

  @PreAuthorize("hasAuthority('READ_PARTICIPANT')")
  @GetMapping("/{teamId}")
  public TeamResponseDTO getTeamById(@PathVariable UUID teamId) {
    return teamService.getTeamById(teamId);
  }

  @PreAuthorize("hasAuthority('UPDATE_OWNED_PARTICIPANT') || hasAuthority('UPDATE_ANY_PARTICIPANT')")
  @PutMapping("/{teamId}")
  public TeamResponseDTO updateTeamById(@AuthenticationPrincipal UserDetails userDetails,
                                        @PathVariable UUID teamId,
                                        @RequestBody @Valid TeamCreateDTO updatedTeam) {
    return teamService.updateTeamById(userDetails, teamId, updatedTeam);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteTeamById(@PathVariable UUID id) {
    teamService.deleteTeamById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}