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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
public abstract class Tournament {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private SportType sportType;

  @Enumerated(EnumType.STRING)
  private TournamentState state;

  private int capacity;

  @ManyToMany
  private List<Participant> participants;

  @OneToMany
  private List<Match> matches;

  protected Tournament(String name, String description, SportType sportType, int capacity) {
    this.name = name;
    this.description = description;
    this.sportType = sportType;
    this.state = TournamentState.REGISTRATION;
    this.capacity = capacity;
    this.participants = new ArrayList<>();
    this.matches = new ArrayList<>();
  }

  public void registerParticipant() {}
}
