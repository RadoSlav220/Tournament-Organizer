package com.fmi.tournament.organizer.controller;

import com.fmi.tournament.organizer.service.RegistrationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Validated
@RestController
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PreAuthorize("hasAuthority('REGISTER_FOR_TOURNAMENT')")
    @PostMapping("/register/{tournamentID}")
    public void register(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID tournamentID){
        registrationService.register(userDetails, tournamentID);
    }

    @PreAuthorize("hasAuthority('UNREGISTER_FROM_TOURNAMENT')")
    @PostMapping("/unregister/{tournamentID}")
    public void unregister(@AuthenticationPrincipal UserDetails userDetails, @PathVariable UUID tournamentID){
        registrationService.unregister(userDetails, tournamentID);
    }
}
