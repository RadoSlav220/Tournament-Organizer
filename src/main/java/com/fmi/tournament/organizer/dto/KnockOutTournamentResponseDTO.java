package com.fmi.tournament.organizer.dto;

import com.fmi.tournament.organizer.model.Category;
import com.fmi.tournament.organizer.model.SportType;
import com.fmi.tournament.organizer.model.TournamentState;
import com.fmi.tournament.organizer.model.TournamentType;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class KnockOutTournamentResponseDTO extends TournamentResponseDTO {
  private List<UUID> advancedToNextRoundParticipantsIds;
  private List<UUID> yetToPlayParticipantsIds;
  private List<UUID> knockedOutParticipantsIds;

  public KnockOutTournamentResponseDTO(UUID id, String name, String description, SportType sportType, Category category, TournamentState state,
                                       TournamentType tournamentType, int capacity, String organizer, List<UUID> participantsIds,
                                       List<UUID> matchesIds, List<UUID> advancedToNextRoundParticipantsIds, List<UUID> yetToPlayParticipantsIds,
                                       List<UUID> knockedOutParticipantsIds) {
    super(id, name, description, sportType, state, tournamentType, category, capacity, organizer, participantsIds, matchesIds);
    this.advancedToNextRoundParticipantsIds = advancedToNextRoundParticipantsIds;
    this.yetToPlayParticipantsIds = yetToPlayParticipantsIds;
    this.knockedOutParticipantsIds = knockedOutParticipantsIds;
  }
}
