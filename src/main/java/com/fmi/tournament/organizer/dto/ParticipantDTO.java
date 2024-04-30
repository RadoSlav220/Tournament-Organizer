package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.SportType;
import lombok.Data;

@Data
public class ParticipantDTO {

  private String name;

  private SportType sportType;
}
