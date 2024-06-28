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
  PARTICIPANT(Set.of()),

  ORGANIZER(Set.of(Permission.CREATE_TOURNAMENT, Permission.READ_OWNED_TOURNAMENT)),

  ADMIN(Set.of(Permission.CREATE_TOURNAMENT, Permission.READ_ALL_TOURNAMENT));

  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = permissions.stream()
        .map(permission -> new SimpleGrantedAuthority(permission.toString()))
        .collect(Collectors.toList());

    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

    return authorities;
  }
}
