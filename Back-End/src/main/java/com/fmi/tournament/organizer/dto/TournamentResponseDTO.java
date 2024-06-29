package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.model.TournamentType;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TournamentResponseDTO {
  private UUID id;
  private String name;
  private String description;
  private SportType sportType;
  private Category category;
  private TournamentState state;
  private TournamentType tournamentType;
  private Category category;
  private int capacity;
  private String organizer;
  private List<UUID> participantsIds;
  private List<UUID> matchesIds;
}
