package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.KnockOutTournament;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnockOutTournamentRepository extends JpaRepository<KnockOutTournament, UUID> {
}
