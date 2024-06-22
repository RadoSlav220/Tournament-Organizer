package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.Match;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
}
