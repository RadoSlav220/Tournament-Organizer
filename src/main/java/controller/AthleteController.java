package controller;

import model.Athlete;
import repository.AthleteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AthleteController {
    @Autowired
    private AthleteRepository repo;

    @GetMapping("/athletes")
    public ResponseEntity<List<Athlete>> getAllAthletes(){
        try {
            List<Athlete> athletes = new ArrayList<>();
            repo.findAll().forEach(athletes::add);

            if(athletes.isEmpty()){
                return new ResponseEntity<>(athletes, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/athletes/{id}")
    public ResponseEntity<Athlete> getAthleteById(@PathVariable UUID id){
        Optional<Athlete> athlete = repo.findById(id);

        if(athlete.isPresent()){
            return new ResponseEntity<>(athlete.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/athletes/create")
    public ResponseEntity<Athlete> createAthlete(@RequestBody Athlete athlete){
        Athlete newAthlete = repo.save(athlete);
        return new ResponseEntity<>(newAthlete, HttpStatus.OK);
    }

    @PatchMapping("/athletes/update/{id}")
    public ResponseEntity<Athlete> updateAthleteById(@PathVariable UUID id, @RequestBody Athlete newAthlete){
        Optional<Athlete> oldAthlete = repo.findById(id);

        if(oldAthlete.isPresent()){
            Athlete updateAthlete = oldAthlete.get();

            updateAthlete.setName(newAthlete.getName());
            updateAthlete.setType(newAthlete.getType());
            updateAthlete.setAge(newAthlete.getAge());
            updateAthlete.setWeight(newAthlete.getWeight());
            updateAthlete.setHeight(newAthlete.getHeight());

            repo.save(updateAthlete);
            return new ResponseEntity<>(updateAthlete, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/athletes/delete/{id}")
    public ResponseEntity<HttpStatus> deleteAthleteById(@PathVariable UUID id){
        repo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
