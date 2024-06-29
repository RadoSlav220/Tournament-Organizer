package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.dto.TournamentResponseDTO;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/registration")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/{participantID}")
    public List<Tournament> getTournamentForRegister(@PathVariable UUID participantID){
        return registrationService.tournamentForRegistration(participantID);
    }

    @PostMapping("/{participantID}/{tournamentID}")
    public void register(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
        registrationService.registration(participantID, tournamentID);
    }

    @GetMapping("/un/{participantID}")
    public List<Tournament> getTournamentForUnregister(@PathVariable UUID participantID){
        return registrationService.tournamentForUnregistration(participantID);
    }

    @PostMapping("/un/{participantID}/{tournamentID}")
    public ResponseEntity<Void> unregister(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
        registrationService.unregistration(participantID, tournamentID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
