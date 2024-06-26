package com.fmi.tournament.organizer.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    @JsonSubTypes.Type(value = League.class, name = "league")
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

  @Enumerated(EnumType.STRING)
  private TournamentType tournamentType;

  @Enumerated(EnumType.STRING)
  private Category category;

  private int capacity;

  private String organizer;

  @ManyToMany
  @JoinTable(
      name = "tournament_participants",
      joinColumns = @JoinColumn(name = "tournament_id"),
      inverseJoinColumns = @JoinColumn(name = "participant_id")
  )
  private List<Participant> participants;

  @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL)
  private List<Match> matches;

  protected Tournament(String name, String description, SportType sportType, TournamentType tournamentType, Category category, int capacity,
                       String organizer) {
    this.name = name;
    this.description = description;
    this.sportType = sportType;
    this.tournamentType = tournamentType;
    this.state = TournamentState.REGISTRATION;
    this.category = category;
    this.capacity = capacity;
    this.organizer = organizer;
    this.participants = new ArrayList<>();
    this.matches = new ArrayList<>();
  }
}
