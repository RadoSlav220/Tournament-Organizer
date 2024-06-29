package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.security.model.Permission;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class TournamentAccessChecker {
  public boolean isTournamentAccessibleForReading(UserDetails userDetails, Tournament tournament) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    return authorities.contains(new SimpleGrantedAuthority(Permission.READ_ANY_TOURNAMENT.toString())) ||
        (authorities.contains(new SimpleGrantedAuthority(Permission.READ_OWNED_TOURNAMENT.toString())) &&
            tournament.getOrganizer().equals(userDetails.getUsername()));
  }

  public boolean isTournamentAccessibleForModification(UserDetails userDetails, Tournament tournament) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    return authorities.contains(new SimpleGrantedAuthority(Permission.MODIFY_ANY_TOURNAMENT.toString())) ||
        (authorities.contains(new SimpleGrantedAuthority(Permission.MODIFY_OWNED_TOURNAMENT.toString())) &&
            tournament.getOrganizer().equals(userDetails.getUsername()));
  }

  public boolean isTournamentAccessibleForDeletion(UserDetails userDetails, Tournament tournament) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    return authorities.contains(new SimpleGrantedAuthority(Permission.DELETE_ANY_TOURNAMENT.toString())) ||
        (authorities.contains(new SimpleGrantedAuthority(Permission.DELETE_OWNED_TOURNAMENT.toString())) &&
            tournament.getOrganizer().equals(userDetails.getUsername()));
  }
}
