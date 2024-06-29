package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class TeamResponseDTO extends ParticipantResponseDTO {
  private int establishmentYear;
  private List<String> players;
  private String manager;

  public TeamResponseDTO(UUID id, String name, String username, SportType sportType, Category category,
                         List<UUID> tournamentsIds, int establishmentYear, List<String> players, String manager) {
    super(id, name, username, sportType, category, tournamentsIds);
    this.establishmentYear = establishmentYear;
    this.players = players;
    this.manager = manager;
  }
}
