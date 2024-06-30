import { Component, Input, OnInit } from '@angular/core';
import { AthleteModel } from '../../model/athelete-model';
import { AthleteService } from '../../service/athlete.service';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TournamentModel } from '../../model/tournament-model';
import { MatchModel } from '../../model/match-model';

@Component({
  selector: 'app-athlete-detail',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './athlete-detail.component.html',
  styleUrl: './athlete-detail.component.css'
})
export class AthleteDetailComponent implements OnInit {
  id!: string;
  athlete!: AthleteModel;
  formAthlete: FormGroup = new FormGroup({});
  formRegistration: FormGroup = new FormGroup({});
  tournaments: TournamentModel[] = [];
  //listMatches: MatchModel[] = [];
  isRegistration: boolean = true;

  constructor(private athleteService: AthleteService, private route: ActivatedRoute){ 

   }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.get();
    //this.listMatches();
    this.formAthlete = new FormGroup({
      'name': new FormControl(''),
      'age': new FormControl(''),
      'height': new FormControl(''),
      'weight': new FormControl(''),
      'sportType': new FormControl(''),
      'category': new FormControl(''),
      'tournaments': new FormControl('')
    });
    
    this.formRegistration = new FormGroup({
      'tournament': new FormControl('')
    });
  }

  @Input()
  get(){
    this.athleteService.getAthleteById(this.id).subscribe(
      resp => {
        if(resp){
          this.athlete = resp;
        }
      }
    );
  }

  @Input()
  listMatches(){
    this.athleteService.getMatches(this.id) 
      .subscribe(resp => {
        if(resp){
    //      this.listMatches = resp;
        }
      });
  }

  openUpdateModal(){
    this.formAthlete.patchValue({
      'name': this.athlete.name,
      'age': this.athlete.age,
      'height': this.athlete.height,
      'weight': this.athlete.weight,
      'sportType': this.athlete.sportType,
      'category': this.athlete.category
    });
  }

  /*update(){
    this.athleteService.updateAthlete(this.formAthlete.value)
    .subscribe(resp =>{
        if(resp){
          this.get();
          this.formAthlete.reset();
        }
      }
    );
  }*/

  openRegistrationModal(){
    this.isRegistration = true;
    this.athleteService.getTournamentForRegister(this.id)
      .subscribe(resp => {
        if(resp){
          this.tournaments = resp;
        }
      })
    this.formRegistration.patchValue({
      'tournament': ''
    });
  }

  register(){
    this.athleteService.register(this.athlete, this.formRegistration.value.tournament)
    .subscribe(resp => {
      if(resp){
        this.tournaments = [];
        this.formRegistration.reset();
        this.get();
      }
    });
  }

  openUnregistrationModal(){
    this.isRegistration = false;
    this.athleteService.getTournamentForUnregister(this.id)
      .subscribe(resp => {
        if(resp){
          this.tournaments = resp;
        }
      })
    this.formRegistration.patchValue({
      'tournament': ''
    });
  }

  unregister(){
    this.athleteService.unregister(this.athlete, this.formRegistration.value.tournament)
    .subscribe(resp => {
      if(resp){
        this.tournaments = [];
        this.formRegistration.reset();
        this.get();
      }
    });
  }
}
