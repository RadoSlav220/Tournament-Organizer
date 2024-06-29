package com.fmi.tournament.organizer.security.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
  PARTICIPANT(Set.of(
      Permission.READ_ANY_TOURNAMENT,
      Permission.CREATE_PARTICIPANT,
      Permission.READ_PARTICIPANT,
      Permission.UPDATE_OWNED_PARTICIPANT,
      Permission.REGISTER_FOR_TOURNAMENT,
      Permission.UNREGISTER_FROM_TOURNAMENT)
  ),

  ORGANIZER(Set.of(
      Permission.CREATE_TOURNAMENT,
      Permission.READ_OWNED_TOURNAMENT,
      Permission.MODIFY_OWNED_TOURNAMENT,
      Permission.DELETE_OWNED_TOURNAMENT,
      Permission.READ_PARTICIPANT)
  ),

  ADMIN(Set.of(
      Permission.CREATE_TOURNAMENT,
      Permission.READ_ANY_TOURNAMENT,
      Permission.MODIFY_ANY_TOURNAMENT,
      Permission.DELETE_ANY_TOURNAMENT,
      Permission.READ_PARTICIPANT,
      Permission.UPDATE_ANY_PARTICIPANT,
      Permission.DELETE_ANY_PARTICIPANT,
      Permission.READ_ANY_AUTH_USER,
      Permission.DELETE_ANY_AUTH_USER)
  );

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = permissions.stream()
        .map(permission -> new SimpleGrantedAuthority(permission.toString()))
        .collect(Collectors.toList());

    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    return authorities;
  }
}
