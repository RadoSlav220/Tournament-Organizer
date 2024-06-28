package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.LeagueCreateDTO;
import com.fmi.tournament.organizer.dto.LeagueResponseDTO;
import com.fmi.tournament.organizer.dto.ScoreDTO;
import com.fmi.tournament.organizer.service.LeagueService;
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

  @PreAuthorize("hasAuthority('READ_ANY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping
  public ResponseEntity<List<LeagueResponseDTO>> getAllLeagues(@AuthenticationPrincipal UserDetails userDetails) {
    List<LeagueResponseDTO> leagues = leagueService.getAllLeagues(userDetails);
    return new ResponseEntity<>(leagues, HttpStatus.OK);
  }

  @PreAuthorize("hasAuthority('READ_ANY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping("/{leagueId}")
  public LeagueResponseDTO getLeagueById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID leagueId) {
    return leagueService.getLeagueById(userDetails, leagueId);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PutMapping("/{leagueId}")
  public LeagueResponseDTO updateLeagueById(@AuthenticationPrincipal UserDetails userDetails,
                                            @PathVariable UUID leagueId,
                                            @RequestBody @Valid LeagueCreateDTO updatedLeague) {
    return leagueService.updateLeagueById(userDetails, leagueId, updatedLeague);
  }

  @PreAuthorize("hasAuthority('DELETE_ANY_TOURNAMENT') || hasAuthority('DELETE_OWNED_TOURNAMENT')")
  @DeleteMapping("/{leagueId}")
  public ResponseEntity<HttpStatus> deleteLeagueById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID leagueId) {
    leagueService.deleteLeagueById(userDetails, leagueId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PostMapping("/{leagueId}/start")
  public LeagueResponseDTO startLeagueById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID leagueId) {
    return leagueService.startLeagueById(userDetails, leagueId);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PostMapping("/{leagueId}/matches/{matchId}/play")
  public LeagueResponseDTO playMatch(@AuthenticationPrincipal UserDetails userDetails,
                                     @PathVariable UUID leagueId,
                                     @PathVariable UUID matchId,
                                     @RequestBody ScoreDTO score) {
    return leagueService.playMatchById(userDetails, leagueId, matchId, score.homeScore(), score.awayScore());
  }
}
