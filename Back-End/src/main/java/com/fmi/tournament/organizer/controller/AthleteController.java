package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.AthleteResponseDTO;
import com.fmi.tournament.organizer.dto.AthleteCreateDTO;
import com.fmi.tournament.organizer.service.AthleteService;
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
@RequestMapping("/athletes")
@CrossOrigin(origins = "http://localhost:4200")
public class AthleteController {

  private final AthleteService athleteService;

  @Autowired
  public AthleteController(AthleteService athleteService) {
      this.athleteService = athleteService;
  }

  @PostMapping
  public ResponseEntity<AthleteResponseDTO> createAthlete(@RequestBody @Valid AthleteCreateDTO athlete) {
    AthleteResponseDTO newAthlete = athleteService.createAthlete(athlete);
    return new ResponseEntity<>(newAthlete, HttpStatus.CREATED);
  }

  @GetMapping
  public List<AthleteResponseDTO> getAllAthletes() {
    return athleteService.getAllAthletes();
  }

  @GetMapping("/{id}")
  public Optional<AthleteResponseDTO> getAthleteById(@PathVariable UUID id) {
    /*Optional<AthleteResponseDTO> fetchedAthlete =*/ return athleteService.getAthleteById(id);
    //return fetchedAthlete.map(athlete -> new ResponseEntity<>(athlete, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PutMapping("/{id}")
  public Optional<AthleteResponseDTO> updateAthleteById(@PathVariable UUID id, @RequestBody @Valid AthleteCreateDTO updatedAthlete) {
    return athleteService.updateAthleteById(id, updatedAthlete);
   }

  @DeleteMapping("/{id}")
  public void deleteAthleteById(@PathVariable UUID id) {
    athleteService.deleteAthleteById(id);
  }
}
