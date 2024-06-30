package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.MatchState;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchResponseDTO {
  private UUID id;
  private LocalDate time;
  private String homeParticipantName;
  private String awayParticipantName;
  private MatchState state;
  private int homeResult;
  private int awayResult;
}
