package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.Team;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
}
