package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KnockOutTournamentService {
  private final KnockOutTournamentRepository knockOutTournamentRepository;

  @Autowired
  public KnockOutTournamentService(KnockOutTournamentRepository knockOutTournamentRepository) {
    this.knockOutTournamentRepository = knockOutTournamentRepository;
  }

  public KnockOutTournament createKnockOutTournament(KnockOutTournamentDTO knockOutTournamentDTO) {
    if (!isPowerOfTwo(knockOutTournamentDTO.getCapacity())) {
      throw new InvalidTournamentCapacityException("The capacity of a KnockOutTournament must be a power of 2.");
    }

    KnockOutTournament knockOutTournament =
        new KnockOutTournament(knockOutTournamentDTO.getName(), knockOutTournamentDTO.getDescription(), knockOutTournamentDTO.getSportType(),
            knockOutTournamentDTO.getCapacity());
    knockOutTournamentRepository.saveAndFlush(knockOutTournament);
    return knockOutTournament;
  }

  private boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }
}
