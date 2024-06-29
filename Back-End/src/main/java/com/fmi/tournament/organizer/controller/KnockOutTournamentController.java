package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.KnockOutTournamentCreateDTO;
import com.fmi.tournament.organizer.dto.KnockOutTournamentResponseDTO;
import com.fmi.tournament.organizer.dto.ScoreDTO;
import com.fmi.tournament.organizer.service.KnockOutTournamentService;
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
@RequestMapping("/knockOutTournaments")
@CrossOrigin(origins = "http://localhost:4200")
public class KnockOutTournamentController {
  private final KnockOutTournamentService knockOutTournamentService;

  @Autowired
  public KnockOutTournamentController(KnockOutTournamentService knockOutTournamentService) {
    this.knockOutTournamentService = knockOutTournamentService;
  }

  @PreAuthorize("hasAuthority('CREATE_TOURNAMENT')")
  @PostMapping
  public ResponseEntity<KnockOutTournamentResponseDTO> createKnockOutTournament(@AuthenticationPrincipal UserDetails userDetails,
                                                                                @RequestBody
                                                                                @Valid KnockOutTournamentCreateDTO knockOutTournamentCreateDTO) {
    KnockOutTournamentResponseDTO knockOutTournament = knockOutTournamentService.createKnockOutTournament(userDetails, knockOutTournamentCreateDTO);
    return new ResponseEntity<>(knockOutTournament, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAuthority('READ_ANY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping
  public List<KnockOutTournamentResponseDTO> getAllKnockOutTournaments(@AuthenticationPrincipal UserDetails userDetails) {
    return knockOutTournamentService.getAllKnockOutTournaments(userDetails);
  }

  @PreAuthorize("hasAuthority('READ_ANY_TOURNAMENT') || hasAuthority('READ_OWNED_TOURNAMENT')")
  @GetMapping("/{tournamentId}")
  public KnockOutTournamentResponseDTO getKnockOutTournamentById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID tournamentId) {
    return knockOutTournamentService.getKnockOutTournamentById(userDetails, tournamentId);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PutMapping("/{tournamentId}")
  public KnockOutTournamentResponseDTO updateKnockOutTournamentById(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @PathVariable UUID tournamentId,
                                                                    @RequestBody @Valid KnockOutTournamentCreateDTO updatedKnockOutTournament) {
    return knockOutTournamentService.updateKnockOutTournamentById(userDetails, tournamentId, updatedKnockOutTournament);
  }

  @PreAuthorize("hasAuthority('DELETE_ANY_TOURNAMENT') || hasAuthority('DELETE_OWNED_TOURNAMENT')")
  @DeleteMapping("/{tournamentId}")
  public ResponseEntity<HttpStatus> deleteKnockOutTournamentById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID tournamentId) {
    knockOutTournamentService.deleteKnockOutTournamentById(userDetails, tournamentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PostMapping("/{tournamentId}/start")
  public KnockOutTournamentResponseDTO startKnockOutTournamentById(@AuthenticationPrincipal UserDetails userDetails,
                                                                   @PathVariable UUID tournamentId) {
    return knockOutTournamentService.startTournamentById(userDetails, tournamentId);
  }

  @PreAuthorize("hasAuthority('MODIFY_ANY_TOURNAMENT') || hasAuthority('MODIFY_OWNED_TOURNAMENT')")
  @PostMapping("/{tournamentId}/matches/{matchId}/play")
  public KnockOutTournamentResponseDTO playMatch(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable UUID tournamentId,
                                                 @PathVariable UUID matchId,
                                                 @RequestBody ScoreDTO score) {
    return knockOutTournamentService.playMatchById(userDetails, tournamentId, matchId, score.homeScore(), score.awayScore());
  }
}
