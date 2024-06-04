package com.fmi.tournament.organizer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "match")
public class Match {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private LocalDate time;

  @ManyToOne
  @JoinColumn(name="tournament_id", nullable=false)
  @JsonBackReference
  private Tournament tournament;

  @ManyToOne
  @JoinColumn(name="home_participant_id", nullable=false)
  @JsonBackReference
  private Participant homeParticipant;

  @ManyToOne
  @JoinColumn(name="away_participant_id", nullable=false)
  @JsonBackReference
  private Participant awayParticipant;

  @Enumerated(EnumType.STRING)
  private MatchState state;

  private int resultHomeParticipant;

  private int resultAwayParticipant;

  public Match(LocalDate time, Tournament tournament, Participant homeParticipant, Participant awayParticipant,
               MatchState state, int resultHomeParticipant, int resultAwayParticipant) {
    this.time = time;
    this.tournament = tournament;
    this.homeParticipant = homeParticipant;
    this.awayParticipant = awayParticipant;
    this.state = state;
    this.resultHomeParticipant = resultHomeParticipant;
    this.resultAwayParticipant = resultAwayParticipant;
  }
}
