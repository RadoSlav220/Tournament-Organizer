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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

  @PostMapping
  public TeamResponseDTO createTeam(@RequestBody @Valid TeamCreateDTO team) {
    return teamService.createTeam(team);
  }

  @GetMapping
  public List<TeamResponseDTO> getAllTeams() {
    return teamService.getAllTeams();
  }

  @GetMapping("/{id}")
  public Optional<TeamResponseDTO> getTeamById(@PathVariable UUID id) {
    return teamService.getTeamById(id);
  }

  @PutMapping("/{id}")
  public Optional<TeamResponseDTO> updateTeamById(@PathVariable UUID id, @RequestBody @Valid TeamCreateDTO updatedTeam) {
    return teamService.updateTeamById(id, updatedTeam);
  }

  @DeleteMapping("/{id}")
  public void deleteTeamById(@PathVariable UUID id) {
    teamService.deleteTeamById(id);
  }
}