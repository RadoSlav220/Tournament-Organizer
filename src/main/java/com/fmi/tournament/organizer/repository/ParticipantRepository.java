package com.fmi.tournament.organizer.repository;

import com.fmi.tournament.organizer.model.Participant;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, UUID> {
}
