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
public class AthleteResponseDTO extends ParticipantResponseDTO {
  private int age;
  private int weight;
  private int height;

  public AthleteResponseDTO(UUID id, String name, String username, SportType sportType, Category category,
                            List<UUID> tournamentsIds, int age, int weight, int height) {
    super(id, name, username, sportType, category, tournamentsIds);
    this.age = age;
    this.weight = weight;
    this.height = height;
  }
}
