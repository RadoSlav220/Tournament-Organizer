package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.BaseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BaseUserDetailsService implements UserDetailsService {
  private final BaseUserRepository userRepository;

  @Autowired
  public BaseUserDetailsService(BaseUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
