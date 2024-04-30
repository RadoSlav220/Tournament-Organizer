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

  public Optional<Athlete> updateAthleteById(UUID id, AthleteDTO newAthlete) {
    Optional<Athlete> oldAthlete = athleteRepository.findById(id);

    if (oldAthlete.isPresent()) {
      Athlete updateAthlete = oldAthlete.get();

      updateAthlete.setName(newAthlete.getName());
      updateAthlete.setSportType(newAthlete.getSportType());
      updateAthlete.setAge(newAthlete.getAge());
      updateAthlete.setWeight(newAthlete.getWeight());
      updateAthlete.setHeight(newAthlete.getHeight());

      return Optional.of(athleteRepository.save(updateAthlete));
    } else {
      return Optional.empty();
    }
  }

  public void deleteAthleteById(UUID id) {
    athleteRepository.deleteById(id);
  }
}
