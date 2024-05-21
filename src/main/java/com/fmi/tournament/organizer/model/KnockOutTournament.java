package com.fmi.tournament.organizer.model;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class KnockOutTournament extends Tournament {
  public KnockOutTournament(String name, String description, SportType sportType, int capacity) {
    super(name, description, sportType, capacity);
  }
}
