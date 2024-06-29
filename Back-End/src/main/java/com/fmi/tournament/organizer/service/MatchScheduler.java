package com.fmi.tournament.organizer.service;

import com.fmi.tournament.organizer.model.KnockOutTournament;
import com.fmi.tournament.organizer.model.League;
import com.fmi.tournament.organizer.model.Match;
import com.fmi.tournament.organizer.model.Participant;
import com.fmi.tournament.organizer.repository.MatchRepository;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatchScheduler {
  private final MatchRepository matchRepository;

  @Autowired
  public MatchScheduler(MatchRepository matchRepository) {
    this.matchRepository = matchRepository;
  }

  public KnockOutTournament scheduleKnockOutTournamentGames(KnockOutTournament tournament, List<UUID> yetToPlayParticipantsIds) {
    for (int i = 0; i < yetToPlayParticipantsIds.size() - 1; i += 2) {
      LocalDate matchTime = LocalDate.now();
      Match match = new Match(matchTime, tournament, yetToPlayParticipantsIds.get(i), yetToPlayParticipantsIds.get(i + 1));
      tournament.getMatches().add(match);
      matchRepository.save(match);
    }
    return tournament;
  }

  public League scheduleLeagueGames(League league) {
    List<UUID> participantsIds = league.getParticipants().stream().map(Participant::getId).toList();

    List<UUID> listHome = new LinkedList<>();
    List<UUID> listAway = new LinkedList<>();

    for (int i = 0; i < participantsIds.size(); i++) {
      if (i % 2 == 0) {
        listHome.add(participantsIds.get(i));
      } else {
        listAway.add(participantsIds.get(i));
      }
    }

    LocalDate matchTime = LocalDate.now();

    int totalRounds = participantsIds.size();
    if (participantsIds.size() % 2 == 0) {
      totalRounds--;
    }

    for (int i = 0; i < totalRounds; i++) {
      scheduleMatches(league, listHome, listAway, matchTime);
      matchTime = matchTime.plusDays(1);
      rotateLists(listHome, listAway, participantsIds.size() % 2 == 0);
    }

    return league;
  }

  private void scheduleMatches(League league, List<UUID> listHome, List<UUID> listAway, LocalDate matchesDate) {
    int numberOfGames = Math.min(listHome.size(), listAway.size());
    for (int i = 0; i < numberOfGames; i++) {
      Match match = new Match(matchesDate, league, listHome.get(i), listAway.get(i));
      league.getMatches().add(match);
      matchRepository.save(match);
    }
  }

  private void rotateLists(List<UUID> listHome, List<UUID> listAway, boolean areParticipantsEven) {
    UUID firstHome;

    if (areParticipantsEven) {
      firstHome = listHome.remove(1);
    } else {
      firstHome = listHome.removeFirst();
    }

    UUID lastAway = listAway.removeLast();
    listAway.addFirst(firstHome);
    listHome.addLast(lastAway);
  }
}
