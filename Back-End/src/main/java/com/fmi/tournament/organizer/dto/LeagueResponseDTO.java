package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.model.TournamentType;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class LeagueResponseDTO extends TournamentResponseDTO {
  private Map<UUID, Integer> results;

  public LeagueResponseDTO(UUID id, String name, String description, SportType sportType, TournamentState state, TournamentType tournamentType,
                           Category category, int capacity, String organizer, List<UUID> participantsIds, List<UUID> matchesIds,
                           Map<UUID, Integer> results) {
    super(id, name, description, sportType, state, tournamentType, category, capacity, organizer, participantsIds, matchesIds);
    this.results = results;
  }
}
