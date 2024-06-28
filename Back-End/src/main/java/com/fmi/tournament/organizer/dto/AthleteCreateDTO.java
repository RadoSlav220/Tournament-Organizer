package com.fmi.tournament.organizer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AthleteCreateDTO extends ParticipantCreateDTO {

  @Min(value = 5, message = "An athlete must be at least 5 years old.")
  @Max(value = 120, message = "An athlete cannot be older than 120.")
  private Integer age;

  @Min(value = 20, message = "An athlete must weigh at least 20 kilos.")
  @Max(value = 400, message = "An athlete cannot weigh more than 400 kilos.")
  private Integer weight;

  @Min(value = 50, message = "An athlete must be at least 50 cm tall.")
  @Max(value = 300, message = "An athlete cannot be taller than 300 cm.")
  private Integer height;
}
