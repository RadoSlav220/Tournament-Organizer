package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.AuthUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/users")
public class AuthUserController {
  private final AuthUserService authUserService;

  @Autowired
  public AuthUserController(AuthUserService authUserService) {
    this.authUserService = authUserService;
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping
  public ResponseEntity<AuthUser> createUser(@RequestBody @Valid AuthUserCreateDTO authUserCreateDTO) {
    AuthUser authUser = authUserService.createUser(authUserCreateDTO);
    return new ResponseEntity<>(authUser, HttpStatus.CREATED);
  }
}
