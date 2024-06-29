package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import com.fmi.tournament.organizer.model.TournamentState;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public abstract class TournamentCreateDTO {
  @NotBlank(message = "Tournament name cannot be blank.")
  private String name;

  private String description;

  @NotNull(message = "Tournament sport type cannot be null.")
  private SportType sportType;

  @NotNull(message = "Tournament category cannot be null.")
  private Category category;

  @NotNull(message = "Tournament capacity cannot be null.")
  @Min(value = 2, message = "Tournament capacity must be at least 2.")
  private Integer capacity;
}
