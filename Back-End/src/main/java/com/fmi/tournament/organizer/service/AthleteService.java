package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.AthleteCreateDTO;
import com.fmi.tournament.organizer.dto.AthleteResponseDTO;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.model.Tournament;
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

  public List<AthleteResponseDTO> getAllAthletes() {
    return athleteRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public Optional<AthleteResponseDTO> getAthleteById(UUID id) {
    return athleteRepository.findById(id).map(this::toResponseDto);
  }

  public AthleteResponseDTO createAthlete(AthleteCreateDTO athleteCreateDTO) {
    Athlete athlete =
        new Athlete(athleteCreateDTO.getName(), athleteCreateDTO.getSportType(), athleteCreateDTO.getAge(), athleteCreateDTO.getWeight(),
            athleteCreateDTO.getHeight(), athleteCreateDTO.getCategory());
    athleteRepository.saveAndFlush(athlete);
    return toResponseDto(athlete);
  }

  public Optional<AthleteResponseDTO> updateAthleteById(UUID id, AthleteCreateDTO updatedAthlete) {
    Optional<Athlete> currentAthlete = athleteRepository.findById(id);
    return currentAthlete.map(athlete -> toResponseDto(updateAthleteDetails(updatedAthlete, athlete)));
  }

  public void deleteAthleteById(UUID id) {
    athleteRepository.deleteById(id);
  }

  private Athlete updateAthleteDetails(AthleteCreateDTO updatedAthlete, Athlete currentAthlete) {
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
    return athleteRepository.saveAndFlush(currentAthlete);
  }

  private AthleteResponseDTO toResponseDto(Athlete athlete) {
    return new AthleteResponseDTO(athlete.getId(),
        athlete.getName(),
        athlete.getSportType(),
        athlete.getCategory(),
        athlete.getTournaments().stream().map(Tournament::getId).toList(),
        athlete.getAge(), athlete.getWeight(), athlete.getHeight());
  }
}
