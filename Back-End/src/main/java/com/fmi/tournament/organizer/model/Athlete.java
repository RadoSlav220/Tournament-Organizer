package com.fmi.tournament.organizer.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Athlete extends Participant {
  private int age;
  private int weight;
  private int height;

  public Athlete(String name, String username, SportType sportType, int age, int weight, int height, Category category) {
    super(name, username, sportType, category);
    this.age = age;
    this.weight = weight;
    this.height = height;
  }
}
