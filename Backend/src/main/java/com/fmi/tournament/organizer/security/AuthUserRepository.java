package com.fmi.tournament.organizer.security;

import com.fmi.tournament.organizer.security.model.AuthUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
  Optional<AuthUser> findByUsername(String username);

  @Modifying
  @Transactional
  @Query(value = "delete from auth_user where username = :username", nativeQuery = true)
  void deleteByUsername(@Param("username") String username);
}
