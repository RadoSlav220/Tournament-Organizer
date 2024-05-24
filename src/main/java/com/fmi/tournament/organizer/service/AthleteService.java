package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.AthleteDTO;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.repository.AthleteRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AthleteService {
  private final AthleteRepository athleteRepository;

  @Autowired
  public AthleteService(AthleteRepository athleteRepository) {
    this.athleteRepository = athleteRepository;
  }

  public List<Athlete> getAllAthletes() {
    return athleteRepository.findAll();
  }

  public Optional<Athlete> getAthleteById(UUID id) {
    return athleteRepository.findById(id);
  }

  public Athlete createAthlete(AthleteDTO athleteDTO) {
    Athlete athlete =
        new Athlete(athleteDTO.getName(), athleteDTO.getSportType(), athleteDTO.getAge(), athleteDTO.getWeight(), athleteDTO.getHeight());
    return athleteRepository.save(athlete);
  }

  public Optional<Athlete> updateAthleteById(UUID id, AthleteDTO updatedAthleteDetails) {
    Optional<Athlete> oldAthlete = athleteRepository.findById(id);
    return oldAthlete.flatMap(athlete -> Optional.of(updateAthleteDetails(updatedAthleteDetails, athlete)));
  }

  public void deleteAthleteById(UUID id) {
    athleteRepository.deleteById(id);
  }

  private Athlete updateAthleteDetails(AthleteDTO updatedAthleteDetails, Athlete currentAthlete) {
    if (updatedAthleteDetails.getName() != null) {
      currentAthlete.setName(updatedAthleteDetails.getName());
    }
    if (updatedAthleteDetails.getSportType() != null) {
      currentAthlete.setSportType(updatedAthleteDetails.getSportType());
    }
    if (updatedAthleteDetails.getAge() != null) {
      currentAthlete.setAge(updatedAthleteDetails.getAge());
    }
    if (updatedAthleteDetails.getWeight() != null) {
      currentAthlete.setWeight(updatedAthleteDetails.getWeight());
    }
    if (updatedAthleteDetails.getHeight() != null) {
      currentAthlete.setHeight(updatedAthleteDetails.getHeight());
    }
    return athleteRepository.save(currentAthlete);
  }
}
