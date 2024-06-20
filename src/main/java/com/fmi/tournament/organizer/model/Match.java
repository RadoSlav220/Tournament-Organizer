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

  private UUID homeParticipantID;

  private UUID awayParticipantID;

  @Enumerated(EnumType.STRING)
  private MatchState state;

  private int resultParticipant1;

  private int resultParticipant2;
}
