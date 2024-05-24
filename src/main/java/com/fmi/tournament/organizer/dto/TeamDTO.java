package com.fmi.tournament.organizer.dto;

import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamDTO extends ParticipantDTO {

  @Min(value = 0, message = "Establishment year cannot be negative.")
  private Integer establishmentYear;

  private String manager;

  private List<String> players;
}
