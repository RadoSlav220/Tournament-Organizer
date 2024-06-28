package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.BaseUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/users")
public class BaseUserController {
  private final BaseUserRepository baseUserRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public BaseUserController(BaseUserRepository baseUserRepository, PasswordEncoder passwordEncoder) {
    this.baseUserRepository = baseUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PreAuthorize("isAnonymous()")
  @PostMapping
  public ResponseEntity<BaseUser> createUser(@RequestBody @Valid BaseUserCreateDTO baseUserCreateDTO) {
    String password = passwordEncoder.encode(baseUserCreateDTO.getPassword());
    BaseUser systemUser = new BaseUser(baseUserCreateDTO.getUsername(), password, baseUserCreateDTO.getRole());
    baseUserRepository.saveAndFlush(systemUser);
    return new ResponseEntity<>(systemUser, HttpStatus.CREATED);
  }
}
