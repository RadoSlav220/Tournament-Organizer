package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.validation.YearNotInFuture;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamCreateDTO extends ParticipantCreateDTO {

  @YearNotInFuture(message = "Establishment year cannot be negative or in the future.")
  private Integer establishmentYear;

  private String manager;

  @NotEmpty(message = "A team must have at least 1 player.")
  private List<String> players;
}
