package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.AthleteDTO;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.service.AthleteService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/athletes")
public class AthleteController {

  private final AthleteService athleteService;

  @Autowired
  public AthleteController(AthleteService athleteService) {
    this.athleteService = athleteService;
  }

  @PostMapping
  public ResponseEntity<Athlete> createAthlete(@RequestBody AthleteDTO athlete) {
    Athlete newAthlete = athleteService.createAthlete(athlete);
    return new ResponseEntity<>(newAthlete, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Athlete>> getAllAthletes() {
    List<Athlete> athletes = athleteService.getAllAthletes();
    if (athletes.isEmpty()) {
      return new ResponseEntity<>(athletes, HttpStatus.NO_CONTENT);
    } else {
      return new ResponseEntity<>(athletes, HttpStatus.OK);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Athlete> getAthleteById(@PathVariable UUID id) {
    Optional<Athlete> fetchedAthlete = athleteService.getAthleteById(id);
    return fetchedAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Athlete> updateAthleteById(@PathVariable UUID id, @RequestBody AthleteDTO newAthlete) {
    Optional<Athlete> updatedAthlete = athleteService.updateAthleteById(id, newAthlete);
    return updatedAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAthleteById(@PathVariable UUID id) {
    athleteService.deleteAthleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}