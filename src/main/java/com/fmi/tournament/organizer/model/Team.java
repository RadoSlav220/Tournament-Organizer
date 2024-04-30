package com.fmi.tournament.organizer.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Team extends Participant {
  private int year;

  @ElementCollection
  private List<String> players;

  private String manager;

  public Team(String name, SportType sportType, int year, List<String> players, String manager) {
    super(name, sportType);
    this.year = year;
    this.players = players;
    this.manager = manager;
  }
}
