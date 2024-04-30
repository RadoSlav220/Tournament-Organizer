package com.fmi.tournament.organizer.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AthleteDTO extends ParticipantDTO {

  private int age;

  private int weight;

  private int height;
}
