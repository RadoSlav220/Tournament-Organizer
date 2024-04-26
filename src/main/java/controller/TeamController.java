package controller;

import model.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TeamController {
    @Autowired
    private TeamRepository repo;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams(){
        try {
            List<Team> teams = new ArrayList<>();
            repo.findAll().forEach(teams::add);

            if(teams.isEmpty()){
                return new ResponseEntity<>(teams, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable UUID id){
        Optional<Team> team = repo.findById(id);

        if(team.isPresent()){
            return new ResponseEntity<>(team.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/teams/create")
    public ResponseEntity<Team> createTeam(@RequestBody Team team){
        Team newTeam = repo.save(team);
        return new ResponseEntity<>(newTeam, HttpStatus.OK);
    }

    @PatchMapping("/teams/update/{id}")
    public ResponseEntity<Team> updateTeamById(@PathVariable UUID id, @RequestBody Team newTeam){
        Optional<Team> oldTeam = repo.findById(id);

        if(oldTeam.isPresent()){
            Team updateTeam = oldTeam.get();
            updateTeam.setName(newTeam.getName());
            updateTeam.setType(newTeam.getType());
            updateTeam.setYear(newTeam.getYear());
            updateTeam.setPlayers(newTeam.getPlayers());
            updateTeam.setManager(newTeam.getManager());

            repo.save(updateTeam);
            return new ResponseEntity<>(updateTeam, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/teams/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTeamById(@PathVariable UUID id){
        repo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
