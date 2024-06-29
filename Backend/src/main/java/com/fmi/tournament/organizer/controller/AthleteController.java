package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.AthleteCreateDTO;
import com.fmi.tournament.organizer.dto.AthleteResponseDTO;
import com.fmi.tournament.organizer.service.AthleteService;
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
@RequestMapping("/athletes")
public class AthleteController {

  private final AthleteService athleteService;

  @Autowired
  public AthleteController(AthleteService athleteService) {
    this.athleteService = athleteService;
  }

  @PreAuthorize("hasAuthority('CREATE_PARTICIPANT')")
  @PostMapping
  public ResponseEntity<AthleteResponseDTO> createAthlete(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody @Valid AthleteCreateDTO athlete) {
    AthleteResponseDTO newAthlete = athleteService.createAthlete(userDetails, athlete);
    return new ResponseEntity<>(newAthlete, HttpStatus.CREATED);
  }

  @PreAuthorize("hasAuthority('READ_PARTICIPANT')")
  @GetMapping
  public List<AthleteResponseDTO> getAllAthletes() {
    return athleteService.getAllAthletes();
  }

  @PreAuthorize("hasAuthority('READ_PARTICIPANT')")
  @GetMapping("/{athleteId}")
  public AthleteResponseDTO getAthleteById(@PathVariable UUID athleteId) {
    return athleteService.getAthleteById(athleteId);
  }

  @PreAuthorize("hasAuthority('UPDATE_OWNED_PARTICIPANT') || hasAuthority('UPDATE_ANY_PARTICIPANT')")
  @PutMapping("/{athleteId}")
  public AthleteResponseDTO updateAthleteById(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable UUID athleteId,
                                              @RequestBody @Valid AthleteCreateDTO updatedAthlete) {
    return athleteService.updateAthleteById(userDetails, athleteId, updatedAthlete);
  }

  @PreAuthorize("hasAuthority('DELETE_ANY_PARTICIPANT')")
  @DeleteMapping("/{athleteId}")
  public ResponseEntity<Void> deleteAthleteById(@PathVariable UUID athleteId) {
    athleteService.deleteAthleteById(athleteId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
