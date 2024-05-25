package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class ParticipantDTO {

  @NotBlank(message = "Participant name cannot be null/empty.")
  private String name;

  @NotNull(message = "Sport type cannot be null.")
  private SportType sportType;
}
