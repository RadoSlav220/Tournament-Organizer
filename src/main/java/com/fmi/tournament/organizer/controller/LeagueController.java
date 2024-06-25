package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.LeagueCreateDTO;
import com.fmi.tournament.organizer.dto.LeagueResponseDTO;
import com.fmi.tournament.organizer.dto.ScoreDTO;
import com.fmi.tournament.organizer.service.LeagueService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/leagues")
public class LeagueController {
  private final LeagueService leagueService;

  @Autowired
  public LeagueController(LeagueService leagueService) {
    this.leagueService = leagueService;
  }

  @PostMapping
  public ResponseEntity<LeagueResponseDTO> createLeague(@RequestBody @Valid LeagueCreateDTO leagueCreateDTO) {
    LeagueResponseDTO league = leagueService.createLeague(leagueCreateDTO);
    return new ResponseEntity<>(league, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<LeagueResponseDTO>> getAllLeagues() {
    List<LeagueResponseDTO> leagues = leagueService.getAllLeagues();
    return new ResponseEntity<>(leagues, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<LeagueResponseDTO> getLeagueById(@PathVariable UUID id) {
    Optional<LeagueResponseDTO> fetchedLeague = leagueService.getLeagueById(id);
    return fetchedLeague.map(league -> new ResponseEntity<>(league, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<LeagueResponseDTO> updateLeagueById(@PathVariable UUID id,
                                                            @RequestBody @Valid LeagueCreateDTO updatedLeague) {
    Optional<LeagueResponseDTO> resultLeague = leagueService.updateLeagueById(id, updatedLeague);
    return resultLeague.map(league -> new ResponseEntity<>(league, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteLeagueById(@PathVariable UUID id) {
    leagueService.deleteLeagueById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{id}/start")
  public ResponseEntity<LeagueResponseDTO> startLeagueById(@PathVariable UUID id) {
    Optional<LeagueResponseDTO> resultLeague = leagueService.startLeagueById(id);
    return resultLeague.map(league -> new ResponseEntity<>(league, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/{leagueId}/matches/{matchId}/play")
  public ResponseEntity<LeagueResponseDTO> playMatch(@PathVariable UUID leagueId, @PathVariable UUID matchId,
                                                     @RequestBody ScoreDTO score) {
    Optional<LeagueResponseDTO> resultLeague = leagueService.playMatchById(leagueId, matchId, score.homeScore(), score.awayScore());
    return resultLeague.map(league -> new ResponseEntity<>(league, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
