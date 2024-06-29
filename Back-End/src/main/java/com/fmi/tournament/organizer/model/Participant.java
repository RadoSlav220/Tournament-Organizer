package com.fmi.tournament.organizer.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Team.class, name = "team"),
    @JsonSubTypes.Type(value = Athlete.class, name = "athlete")
})
public abstract class Participant {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  @Column(unique = true, nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Enumerated(EnumType.STRING)
  private SportType sportType;

  @ManyToMany(mappedBy = "participants")
  private List<Tournament> tournaments;

  protected Participant(String name, String username, SportType sportType, Category category) {
    this.name = name;
    this.username = username;
    this.sportType = sportType;
    this.category = category;
    tournaments = new ArrayList<>();
  }

  public String getObjectType() {
    return this.getClass().getSimpleName();
  }
}
