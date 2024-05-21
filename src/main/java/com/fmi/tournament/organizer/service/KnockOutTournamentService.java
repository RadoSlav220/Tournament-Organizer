package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.KnockOutTournamentDTO;
import com.fmi.tournament.organizer.exception.InvalidTournamentCapacityException;
import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.repository.KnockOutTournamentRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  public List<KnockOutTournament> getAllKnockOutTournaments() {
    return knockOutTournamentRepository.findAll();
  }

  public Optional<KnockOutTournament> getKnockOutTournamentById(UUID id) {
    return knockOutTournamentRepository.findById(id);
  }

  public Optional<KnockOutTournament> updateKnockOutTournamentById(UUID id, KnockOutTournamentDTO updatedTournamentDetails) {
    Optional<KnockOutTournament> oldTournament = knockOutTournamentRepository.findById(id);
    return oldTournament.flatMap(tournament -> Optional.of(updateTournamentDetails(updatedTournamentDetails, tournament)));
  }

  public void deleteKnockOutTournamentById(UUID id) {
    knockOutTournamentRepository.deleteById(id);
  }

  private KnockOutTournament updateTournamentDetails(KnockOutTournamentDTO updatedTournamentDetails, KnockOutTournament currentTournament) {
    if (currentTournament.getParticipants().size() > updatedTournamentDetails.getCapacity()) {
      throw new InvalidTournamentCapacityException("The updated capacity cannot be less than the number of participants already enrolled.");
    } else if (updatedTournamentDetails.getCapacity() != 0 && !isPowerOfTwo(updatedTournamentDetails.getCapacity())) {
      throw new InvalidTournamentCapacityException("The updated capacity must be a power of two.");
    }

    if (updatedTournamentDetails.getName() != null) {
      currentTournament.setName(updatedTournamentDetails.getName());
    }
    if (updatedTournamentDetails.getDescription() != null) {
      currentTournament.setDescription(updatedTournamentDetails.getDescription());
    }
    if (updatedTournamentDetails.getSportType() != null) {
      currentTournament.setSportType(updatedTournamentDetails.getSportType());
    }
    if (updatedTournamentDetails.getCapacity() != 0) {
      currentTournament.setCapacity(updatedTournamentDetails.getCapacity());
    }

    return knockOutTournamentRepository.save(currentTournament);
  }

  private boolean isPowerOfTwo(int n) {
    return n > 0 && (n & (n - 1)) == 0;
  }
}
