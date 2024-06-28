package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.BaseUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUser, Long> {
  Optional<BaseUser> findByUsername(String username);
}
