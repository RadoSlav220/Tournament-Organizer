package com.fmi.tournament.organizer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  private Date time;

  @ManyToOne
  private Tournament tournament;

  private UUID homeParticipantID;

  private UUID awayParticipantID;

  @Enumerated(EnumType.STRING)
  private MatchState state;

  private int resultParticipant1;

  private int resultParticipant2;
}
