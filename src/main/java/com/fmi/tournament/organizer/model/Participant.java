package com.fmi.tournament.organizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public abstract class Participant {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Enumerated(EnumType.STRING)
  private SportType sportType;

  @ManyToMany
  private List<Tournament> tournaments;

  @OneToMany
  private List<Match> matches;

  protected Participant(String name, SportType sportType) {
    this.name = name;
    this.sportType = sportType;
    tournaments = new ArrayList<>();
    matches = new ArrayList<>();
  }
}
