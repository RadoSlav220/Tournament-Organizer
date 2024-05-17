package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.SportType;
import lombok.Data;

@Data
public abstract class ParticipantDTO {

  private String name;

  private SportType sportType;
}
