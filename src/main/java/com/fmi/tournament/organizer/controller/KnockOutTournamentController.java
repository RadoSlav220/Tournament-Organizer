package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.service.KnockOutTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/knockOutTournament")
public class KnockOutTournamentController {
  private final KnockOutTournamentService knockOutTournamentService;

  @Autowired
  public KnockOutTournamentController(KnockOutTournamentService knockOutTournamentService) {
    this.knockOutTournamentService = knockOutTournamentService;
  }

  @PostMapping("/create")
  public ResponseEntity<KnockOutTournament> createKnockOutTournament(@RequestBody KnockOutTournamentDTO knockOutTournamentDTO) {
    KnockOutTournament knockOutTournament = knockOutTournamentService.createKnockOutTournament(knockOutTournamentDTO);
    return new ResponseEntity<>(knockOutTournament, HttpStatus.OK);
  }
}
