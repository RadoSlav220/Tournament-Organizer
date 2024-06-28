package com.fmi.tournament.organizer.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.MapKeyColumn;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class League extends Tournament {
  @ElementCollection
  @MapKeyColumn(name = "participant_id")
  @Column(name = "points")
  Map<UUID, Integer> results;

  public League(String name, String description, SportType sportType, int capacity, String organizer) {
    super(name, description, sportType, capacity, organizer);
    this.results = new HashMap<>();
  }
}
