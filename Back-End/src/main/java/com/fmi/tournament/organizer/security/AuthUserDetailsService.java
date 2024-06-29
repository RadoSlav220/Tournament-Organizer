package com.fmi.tournament.organizer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
  private final AuthUserRepository authUserRepository;

  @Autowired
  public AuthUserDetailsService(AuthUserRepository authUserRepository) {
    this.authUserRepository = authUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return authUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
