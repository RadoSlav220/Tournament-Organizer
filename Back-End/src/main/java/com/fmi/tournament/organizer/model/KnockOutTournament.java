package com.fmi.tournament.organizer.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class KnockOutTournament extends Tournament {
  @ElementCollection
  private List<UUID> advancedToNextRoundParticipantsIds;

  @ElementCollection
  private List<UUID> yetToPlayParticipantsIds;

  @ElementCollection
  private List<UUID> knockedOutParticipantsIds;

  public KnockOutTournament(String name, String description, SportType sportType, TournamentType tournamentType, Category category, int capacity,
                            String organizer) {
    super(name, description, sportType, tournamentType, category, capacity, organizer);
    this.advancedToNextRoundParticipantsIds = new ArrayList<>();
    this.yetToPlayParticipantsIds = new ArrayList<>();
    this.knockedOutParticipantsIds = new ArrayList<>();
  }
}
