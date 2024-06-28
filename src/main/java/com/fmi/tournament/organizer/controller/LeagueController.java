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
@RequestMapping("/leagues")
public class LeagueController {
  private final LeagueService leagueService;

  @Autowired
  public LeagueController(LeagueService leagueService) {
    this.leagueService = leagueService;
  }

  @PreAuthorize("hasAuthority('CREATE_TOURNAMENT')")
  @PostMapping
  public ResponseEntity<LeagueResponseDTO> createLeague(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody @Valid LeagueCreateDTO leagueCreateDTO) {
    LeagueResponseDTO league = leagueService.createLeague(userDetails, leagueCreateDTO);
    return new ResponseEntity<>(league, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAuthority('READ_EVERY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping
  public ResponseEntity<List<LeagueResponseDTO>> getAllLeagues(@AuthenticationPrincipal UserDetails userDetails) {
    List<LeagueResponseDTO> leagues = leagueService.getAllLeagues(userDetails);
    return new ResponseEntity<>(leagues, HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('READ_EVERY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping("/{id}")
  public LeagueResponseDTO getLeagueById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID id) {
    return leagueService.getLeagueById(userDetails, id);
  }

  @PreAuthorize("hasAuthority('MODIFY_EVERY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PutMapping("/{id}")
  public LeagueResponseDTO updateLeagueById(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable UUID id,
                                            @RequestBody @Valid LeagueCreateDTO updatedLeague) {
    return leagueService.updateLeagueById(userDetails, id, updatedLeague);
  }

  @PreAuthorize("hasAuthority('DELETE_EVERY_TOURNAMENT') || hasAuthority('DELETE_OWNED_TOURNAMENT')")
  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteLeagueById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID leagueId) {
    leagueService.deleteLeagueById(userDetails, leagueId);
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
