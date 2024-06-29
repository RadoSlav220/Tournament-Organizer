package com.fmi.tournament.organizer.security.dto;

import com.fmi.tournament.organizer.security.model.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserResponseDTO {
  private String username;
  private Role role;
  private List<String> authorities;
}
