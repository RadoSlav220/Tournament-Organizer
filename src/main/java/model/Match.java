package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Date time;
    private Participant participant1;
    private Participant participant2;
    private MatchState state;
    private int resultParticipant1;
    private int resultParticipant2;
}
