package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.AthleteDTO;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.service.AthleteService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/athletes")
public class AthleteController {

  private final AthleteService athleteService;
  private final TournamentService tournamentService;

  @Autowired
  public AthleteController(AthleteService athleteService, TournamentService tournamentService) {
      this.athleteService = athleteService;
      this.tournamentService = tournamentService;
  }

  @PostMapping
  public ResponseEntity<Athlete> createAthlete(@RequestBody @Valid AthleteDTO athlete) {
    Athlete newAthlete = athleteService.createAthlete(athlete);
    return new ResponseEntity<>(newAthlete, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<Athlete>> getAllAthletes() {
    List<Athlete> athletes = athleteService.getAllAthletes();
    return new ResponseEntity<>(athletes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Athlete> getAthleteById(@PathVariable UUID id) {
    Optional<Athlete> fetchedAthlete = athleteService.getAthleteById(id);
    return fetchedAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Athlete> updateAthleteById(@PathVariable UUID id, @RequestBody @Valid AthleteDTO updatedAthlete) {
    Optional<Athlete> resultAthlete = athleteService.updateAthleteById(id, updatedAthlete);
    return resultAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAthleteById(@PathVariable UUID id) {
    athleteService.deleteAthleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/{participantID}/{tournamentID}")
  public ResponseEntity<Void> registration(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
    tournamentService.registration(participantID, tournamentID);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
