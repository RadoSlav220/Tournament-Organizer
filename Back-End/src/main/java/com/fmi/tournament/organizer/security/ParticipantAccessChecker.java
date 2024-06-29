package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.security.model.Permission;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class ParticipantAccessChecker {
  public boolean isParticipantAccessibleForModification(UserDetails userDetails, Participant participant) {
    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    return authorities.contains(new SimpleGrantedAuthority(Permission.UPDATE_ANY_PARTICIPANT.toString())) ||
        (authorities.contains(new SimpleGrantedAuthority(Permission.UPDATE_OWNED_PARTICIPANT.toString())) &&
            participant.getUsername().equals(userDetails.getUsername()));
  }
}
