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
  private int establishmentYear;

  @ElementCollection
  private List<String> players;

  private String manager;

  public Team(String name, String username, SportType sportType, int establishmentYear, List<String> players, String manager, Category category) {
    super(name, username, sportType, category);
    this.establishmentYear = establishmentYear;
    this.players = players;
    this.manager = manager;
  }
}
