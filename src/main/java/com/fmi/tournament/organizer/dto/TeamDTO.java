package com.fmi.tournament.organizer.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeamDTO extends ParticipantDTO {

  private Integer year;

  private String manager;

  private List<String> players;
}
