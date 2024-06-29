import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { TeamModel } from '../../model/team-model';
import { TournamentModel } from '../../model/tournament-model';
import { TeamService } from '../../service/team.service';

@Component({
  selector: 'app-team-detail',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './team-detail.component.html',
  styleUrl: './team-detail.component.css'
})
export class TeamDetailComponent {
  id!: string;
  team!: TeamModel;
  formTeam: FormGroup = new FormGroup({});
  formRegistration: FormGroup = new FormGroup({});
  tournaments: TournamentModel[] = [];
  //listMatches: MatchModel[] = [];
  isRegistration: boolean = true;

  constructor(private teamService: TeamService, private route: ActivatedRoute){ 

   }

  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.get();
    //this.listMatches();
    this.formTeam = new FormGroup({
      'name': new FormControl(''),
      'sportType': new FormControl(''),
      'category': new FormControl(''),
      'manager': new FormControl(''),
      'establishmentYear': new FormControl(''),
      'players': new FormControl('')
    });
    
    this.formRegistration = new FormGroup({
      'tournament': new FormControl('')
    });
  }

  @Input()
  get(){
    this.teamService.getTeamById(this.id).subscribe(
      resp => {
        if(resp){
          this.team = resp;
        }
      }
    );
  }

  @Input()
  listMatches(){
    this.teamService.getMatches(this.id) 
      .subscribe(resp => {
        if(resp){
    //      this.listMatches = resp;
        }
      });
  }

  openUpdateModal(){
    this.formTeam.patchValue({
      'name': this.team.name,
      'sportType': this.team.sportType,
      'category': this.team.category,
      'manager': this.team.manager,
      'establishmentYear': this.team.establishmentYear,
      'players': this.team.players
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
    this.teamService.getTournamentForRegister(this.id)
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
    this.teamService.register(this.team, this.formRegistration.value.tournament)
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
    this.teamService.getTournamentForUnregister(this.id)
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
    this.teamService.unregister(this.team, this.formRegistration.value.tournament)
    .subscribe(resp => {
      if(resp){
        this.tournaments = [];
        this.formRegistration.reset();
        this.get();
      }
    });
  }
}
