package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.SportType;
import lombok.Data;

@Data
public abstract class TournamentDTO {
  private String name;

  private String description;

  private SportType sportType;

  private int capacity;
}
