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

  public Optional<Athlete> updateAthleteById(UUID id, AthleteDTO updatedAthlete) {
    Optional<Athlete> currentAthlete = athleteRepository.findById(id);
    return currentAthlete.flatMap(athlete -> Optional.of(updateAthleteDetails(updatedAthlete, athlete)));
  }

  public void deleteAthleteById(UUID id) {
    athleteRepository.deleteById(id);
  }

  private Athlete updateAthleteDetails(AthleteDTO updatedAthlete, Athlete currentAthlete) {
    if (updatedAthlete.getName() != null) {
      currentAthlete.setName(updatedAthlete.getName());
    }
    if (updatedAthlete.getSportType() != null) {
      currentAthlete.setSportType(updatedAthlete.getSportType());
    }
    if (updatedAthlete.getAge() != null) {
      currentAthlete.setAge(updatedAthlete.getAge());
    }
    if (updatedAthlete.getWeight() != null) {
      currentAthlete.setWeight(updatedAthlete.getWeight());
    }
    if (updatedAthlete.getHeight() != null) {
      currentAthlete.setHeight(updatedAthlete.getHeight());
    }
    return athleteRepository.save(currentAthlete);
  }
}
