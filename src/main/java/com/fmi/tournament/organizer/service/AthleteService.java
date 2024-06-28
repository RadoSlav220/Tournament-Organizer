package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.dto.AthleteCreateDTO;
import com.fmi.tournament.organizer.dto.AthleteResponseDTO;
import com.fmi.tournament.organizer.exception.ForbiddenActionException;
import com.fmi.tournament.organizer.model.Athlete;
import com.fmi.tournament.organizer.model.Tournament;
import com.fmi.tournament.organizer.repository.AthleteRepository;
import com.fmi.tournament.organizer.security.ParticipantAccessChecker;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AthleteService {
  private static final String FORBIDDEN_ACTION_ERROR_MESSAGE =
      "Athlete with id '%s' exists but you have no permissions to execute the requested action.";
  private static final String ATHLETE_NOT_FOUND_ERROR_MESSAGE = "Athlete with id '%s' does not exist.";
  private final AthleteRepository athleteRepository;
  private final ParticipantAccessChecker participantAccessChecker;

  @Autowired
  public AthleteService(AthleteRepository athleteRepository, ParticipantAccessChecker participantAccessChecker) {
    this.athleteRepository = athleteRepository;
    this.participantAccessChecker = participantAccessChecker;
  }

  public AthleteResponseDTO createAthlete(UserDetails userDetails, AthleteCreateDTO athleteCreateDTO) {
    Athlete athlete =
        new Athlete(athleteCreateDTO.getName(), userDetails.getUsername(), athleteCreateDTO.getSportType(), athleteCreateDTO.getAge(),
            athleteCreateDTO.getWeight(), athleteCreateDTO.getHeight(), athleteCreateDTO.getCategory());

    athleteRepository.saveAndFlush(athlete);

    return toResponseDto(athlete);
  }

  public List<AthleteResponseDTO> getAllAthletes() {
    return athleteRepository.findAll().stream().map(this::toResponseDto).toList();
  }

  public AthleteResponseDTO getAthleteById(UUID athleteId) {
    Athlete athlete =
        athleteRepository.findById(athleteId).orElseThrow(() -> new NoSuchElementException(ATHLETE_NOT_FOUND_ERROR_MESSAGE.formatted(athleteId)));
    return toResponseDto(athlete);
  }

  public AthleteResponseDTO updateAthleteById(UserDetails userDetails, UUID athleteId, AthleteCreateDTO updatedAthlete) {
    Athlete currentAthlete =
        athleteRepository.findById(athleteId).orElseThrow(() -> new NoSuchElementException(ATHLETE_NOT_FOUND_ERROR_MESSAGE.formatted(athleteId)));

    if (!participantAccessChecker.isParticipantAccessibleForModification(userDetails, currentAthlete)) {
      throw new ForbiddenActionException(FORBIDDEN_ACTION_ERROR_MESSAGE.formatted(athleteId));
    }

    return toResponseDto(updateAthleteDetails(updatedAthlete, currentAthlete));
  }

  public void deleteAthleteById(UUID athleteId) {
    athleteRepository.deleteById(athleteId);
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
        athlete.getUsername(),
        athlete.getSportType(),
        athlete.getCategory(),
        athlete.getTournaments().stream().map(Tournament::getId).toList(),
        athlete.getAge(), athlete.getWeight(), athlete.getHeight());
  }
}
