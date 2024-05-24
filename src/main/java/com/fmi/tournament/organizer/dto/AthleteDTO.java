package com.fmi.tournament.organizer.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AthleteDTO extends ParticipantDTO {

  private Integer age;

  private Integer weight;

  private Integer height;
}
