package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.KnockOutTournamentCreateDTO;
import com.fmi.tournament.organizer.dto.KnockOutTournamentResponseDTO;
import com.fmi.tournament.organizer.dto.ScoreDTO;
import com.fmi.tournament.organizer.service.KnockOutTournamentService;
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
@RequestMapping("/knockOutTournament")
@CrossOrigin(origins = "http://localhost:4200")
public class KnockOutTournamentController {
  private final KnockOutTournamentService knockOutTournamentService;

  @Autowired
  public KnockOutTournamentController(KnockOutTournamentService knockOutTournamentService) {
    this.knockOutTournamentService = knockOutTournamentService;
  }

  @PostMapping
  public KnockOutTournamentResponseDTO createKnockOutTournament(
      @RequestBody @Valid KnockOutTournamentCreateDTO knockOutTournamentCreateDTO) {
    return knockOutTournamentService.createKnockOutTournament(knockOutTournamentCreateDTO);
  }

  @GetMapping
  public List<KnockOutTournamentResponseDTO> getAllKnockOutTournaments() {
    return knockOutTournamentService.getAllKnockOutTournaments();
  }

  @GetMapping("/{id}")
  public Optional<KnockOutTournamentResponseDTO> getKnockOutTournamentById(@PathVariable UUID id) {
    return knockOutTournamentService.getKnockOutTournamentById(id);
  }

  @PutMapping("/{id}")
  public Optional<KnockOutTournamentResponseDTO> updateKnockOutTournamentById(@PathVariable UUID id,
                                                                                    @RequestBody
                                                                                    @Valid KnockOutTournamentCreateDTO updatedKnockOutTournament) {
    return knockOutTournamentService.updateKnockOutTournamentById(id, updatedKnockOutTournament);
  }

  @DeleteMapping("/{id}")
  public void deleteKnockOutTournamentById(@PathVariable UUID id) {
    knockOutTournamentService.deleteKnockOutTournamentById(id);
  }

  @PostMapping("/{id}/start")
  public Optional<KnockOutTournamentResponseDTO> startKnockOutTournamentById(@PathVariable UUID id) {
    return knockOutTournamentService.startTournamentById(id);
  }

  @PostMapping("/{tournamentId}/matches/{matchId}/play")
  public Optional<KnockOutTournamentResponseDTO> playMatch(@PathVariable UUID tournamentId, @PathVariable UUID matchId,
                                                                 @RequestBody ScoreDTO score) {
    return knockOutTournamentService.playMatchById(tournamentId, matchId, score.homeScore(),
        score.awayScore());
  }
}
