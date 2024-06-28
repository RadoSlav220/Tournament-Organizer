package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParticipantResponseDTO {
  private UUID id;
  private String name;
  private SportType sportType;
  private Category category;
  private List<UUID> tournamentsIds;
}
