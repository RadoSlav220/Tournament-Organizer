package com.fmi.tournament.organizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "match")
public class Match {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private LocalDate time;

  @ManyToOne
  @JoinColumn(name = "tournament_id", nullable = false)
  private Tournament tournament;

  private UUID homeParticipantID;

  private UUID awayParticipantID;

  @Enumerated(EnumType.STRING)
  private MatchState state;

  private int homeResult;

  private int awayResult;

  public Match(LocalDate time, Tournament tournament, UUID homeParticipantID, UUID awayParticipantID) {
    this.time = time;
    this.tournament = tournament;
    this.homeParticipantID = homeParticipantID;
    this.awayParticipantID = awayParticipantID;
    state = MatchState.NOT_STARTED;
  }
}
