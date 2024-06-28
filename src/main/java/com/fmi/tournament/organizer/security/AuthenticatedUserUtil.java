package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.Role;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUserUtil {
  public static String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
      return userDetails.getUsername();
    }
    return null;
  }
}
