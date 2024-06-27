package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("{participantID}/{tournamentID}")
    public ResponseEntity<Void> register(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
        registrationService.registration(participantID, tournamentID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("un/{participantID}/{tournamentID}")
    public ResponseEntity<Void> unregister(@PathVariable UUID participantID, @PathVariable UUID tournamentID){
        registrationService.unregistration(participantID, tournamentID);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
