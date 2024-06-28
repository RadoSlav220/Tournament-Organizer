package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.AuthUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  Optional<AuthUser> findByUsername(String username);
}
