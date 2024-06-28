package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.AuthUser;
import com.fmi.tournament.organizer.security.model.Role;
import java.util.Optional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {
  private final AuthUserRepository authUserRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthUserService(AuthUserRepository authUserRepository, PasswordEncoder passwordEncoder) {
    this.authUserRepository = authUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public AuthUser createUser(AuthUserCreateDTO authUserCreateDTO) {
    String password = passwordEncoder.encode(authUserCreateDTO.getPassword());
    AuthUser authUser = new AuthUser(authUserCreateDTO.getUsername(), password, authUserCreateDTO.getRole());
    return authUserRepository.saveAndFlush(authUser);
  }

  @EventListener(ApplicationReadyEvent.class)
  private void createAdmin() {
    Optional<AuthUser> maybeAdmin = authUserRepository.findByUsername("admin");
    if (maybeAdmin.isEmpty()) {
      String adminPassword = passwordEncoder.encode("1234");
      AuthUser admin = new AuthUser("admin", adminPassword, Role.ADMIN);
      authUserRepository.saveAndFlush(admin);
    }
  }
}
