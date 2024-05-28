package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

}
