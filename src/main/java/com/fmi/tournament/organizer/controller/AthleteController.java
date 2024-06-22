package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.AthleteResponseDTO;
import com.fmi.tournament.organizer.dto.AthleteCreateDTO;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.service.AthleteService;
import com.fmi.tournament.organizer.service.ParticipantService;
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
  private final ParticipantService participantService;

  @Autowired
  public AthleteController(AthleteService athleteService, ParticipantService participantService) {
      this.athleteService = athleteService;
      this.participantService = participantService;
  }

  @PostMapping
  public ResponseEntity<AthleteResponseDTO> createAthlete(@RequestBody @Valid AthleteCreateDTO athlete) {
    AthleteResponseDTO newAthlete = athleteService.createAthlete(athlete);
    return new ResponseEntity<>(newAthlete, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<AthleteResponseDTO>> getAllAthletes() {
    List<AthleteResponseDTO> athletes = athleteService.getAllAthletes();
    return new ResponseEntity<>(athletes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AthleteResponseDTO> getAthleteById(@PathVariable UUID id) {
    Optional<AthleteResponseDTO> fetchedAthlete = athleteService.getAthleteById(id);
    return fetchedAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AthleteResponseDTO> updateAthleteById(@PathVariable UUID id, @RequestBody @Valid AthleteCreateDTO updatedAthlete) {
    Optional<AthleteResponseDTO> resultAthlete = athleteService.updateAthleteById(id, updatedAthlete);
    return resultAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteAthleteById(@PathVariable UUID id) {
    athleteService.deleteAthleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @PostMapping("/registration/{participantID}/{tournamentID}")
  public ResponseEntity<Void> registration(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
    participantService.registration(participantID, tournamentID);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/unregistration/{participantID}/{tournamentID}")
  public ResponseEntity<Void> unregistration(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
    participantService.unregistration(participantID, tournamentID);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
