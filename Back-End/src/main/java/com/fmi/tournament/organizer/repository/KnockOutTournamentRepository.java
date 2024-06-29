package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.League;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KnockOutTournamentRepository extends JpaRepository<KnockOutTournament, UUID> {
  List<KnockOutTournament> findByOrganizer(String organizer);
}
