package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseUserCreateDTO {
  @NotEmpty(message = "Username cannot be empty.")
  private String username;

  @NotEmpty(message = "Password cannot be empty.")
  private String password;

  @NotNull(message = "User role cannot be null.")
  private Role role;
}
