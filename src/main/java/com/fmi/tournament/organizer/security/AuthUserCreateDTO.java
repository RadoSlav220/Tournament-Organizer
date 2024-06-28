package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.Role;
import com.fmi.tournament.organizer.validation.NotEqual;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthUserCreateDTO {
  @NotEmpty(message = "Username cannot be empty.")
  private String username;

  @NotEmpty(message = "Password cannot be empty.")
  private String password;

  @NotNull(message = "User role cannot be null.")
  @NotEqual(enumClass = Role.class, forbiddenValue = "ADMIN", message = "You are not allowed to create user with admin role.")
  private Role role;
}
