package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.service.KnockOutTournamentService;
import com.fmi.tournament.organizer.service.TournamentService;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/knockOutTournament")
public class KnockOutTournamentController {
  private final KnockOutTournamentService knockOutTournamentService;
  private final TournamentService tournamentService;

  @Autowired
  public KnockOutTournamentController(KnockOutTournamentService knockOutTournamentService, TournamentService tournamentService) {
      this.knockOutTournamentService = knockOutTournamentService;
      this.tournamentService = tournamentService;
  }

  @PostMapping
  public ResponseEntity<KnockOutTournament> createKnockOutTournament(@RequestBody @Valid KnockOutTournamentDTO knockOutTournamentDTO) {
    KnockOutTournament knockOutTournament = knockOutTournamentService.createKnockOutTournament(knockOutTournamentDTO);
    return new ResponseEntity<>(knockOutTournament, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<KnockOutTournament>> getAllKnockOutTournaments() {
    List<KnockOutTournament> tournaments = knockOutTournamentService.getAllKnockOutTournaments();
    return new ResponseEntity<>(tournaments, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<KnockOutTournament> getKnockOutTournamentById(@PathVariable UUID id) {
    Optional<KnockOutTournament> fetchedTournament = knockOutTournamentService.getKnockOutTournamentById(id);
    return fetchedTournament.map(tournament -> new ResponseEntity<>(tournament, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<KnockOutTournament> updateKnockOutTournamentById(@PathVariable UUID id,
                                                                         @RequestBody @Valid KnockOutTournamentDTO updatedKnockOutTournament) {
    Optional<KnockOutTournament> resultTournament = knockOutTournamentService.updateKnockOutTournamentById(id, updatedKnockOutTournament);
    return resultTournament.map(tournament -> new ResponseEntity<>(tournament, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<HttpStatus> deleteKnockOutTournamentById(@PathVariable UUID id) {
    knockOutTournamentService.deleteKnockOutTournamentById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/startTournament/{id}")
  public ResponseEntity<HttpStatus> startTournament(@PathVariable UUID id) {
    knockOutTournamentService.startTournament(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/{tournamentID}/{matchID}/{resultHomeParticipant}/{resultAwayParticipant}")
  public ResponseEntity<HttpStatus> playMatch(@PathVariable UUID tournamentID, @PathVariable UUID matchID,
                                                      @PathVariable int resultHomeParticipant, @PathVariable
                                                        int resultAwayParticipant) {
    tournamentService.playMatch(matchID, resultHomeParticipant, resultAwayParticipant);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
