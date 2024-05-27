package com.fmi.tournament.organizer.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = KnockOutTournament.class, name = "knockOutTournament"),
})
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
  @JoinTable(
          name="tournament_participants",
          joinColumns = @JoinColumn(name = "tournament_id"),
          inverseJoinColumns = @JoinColumn(name = "participant_id")
  )
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
}
