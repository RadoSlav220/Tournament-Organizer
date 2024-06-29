import { Component, Input, OnInit } from '@angular/core';
import { TournamentModel } from '../../model/tournament-model';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TournamentService } from '../../service/tournament.service';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-tournament',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './tournament.component.html',
  styleUrl: './tournament.component.css'
})
export class TournamentComponent implements OnInit {
  listTournament: TournamentModel[] = [];
  formTournament: FormGroup = new FormGroup({});
  isUpdate: boolean = false;

  constructor(private tournamentService: TournamentService) {  }

  ngOnInit(): void {
    this.list();
    this.formTournament = new FormGroup({
      'id': new FormControl(''),
      'name': new FormControl(''),
      'description': new FormControl(''),
      'sportType': new FormControl(''),
      'state': new FormControl(''),
      'category': new FormControl(''),
      'capacity': new FormControl(''),
      'participants': new FormControl(''),
      'matches': new FormControl('')
    })
  }

  @Input()
  list(){
    this.tournamentService.getTournaments() 
      .subscribe(resp => {
        this.listTournament = resp;
      });
  }

  newTournament(){
    this.tournamentService.createTournament(this.formTournament.value).subscribe(
      resp => {
        if(resp){
          this.listTournament.push(resp);
          this.formTournament.reset();
        }
      }
    );
  }

  openNewTournament(){
    this.formTournament.reset();
    this.isUpdate = false;
  }

  openUpdateModal(tournament: TournamentModel){
    this.isUpdate = true;
    this.formTournament.patchValue({
      'id': tournament.id,
      'name': tournament.name,
      'description': tournament.description,
      'sportType': tournament.sportType,
      'state': tournament.state,
      'category': tournament.category,
      'capacity': tournament.capacity,
      'participant': tournament.participants
    });
  }

  update(){
    this.tournamentService.updateTournament(this.formTournament.value).subscribe(
      resp =>{
        if(resp){
          this.list();
          this.formTournament.reset();
        }
      }
    );
  }

  delete(id: string){
    this.tournamentService.deleteTournament(id).subscribe(
      () =>{
        this.listTournament = this.listTournament.filter(tournament => tournament.id != id);
      }
    );
  }

  startTournament(tournament: TournamentModel){
    this.tournamentService.startTournament(tournament).subscribe(
      () => {
        this.list();
      }
    );
  }
}
