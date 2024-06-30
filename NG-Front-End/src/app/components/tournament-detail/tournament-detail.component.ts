import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { MatchModel } from '../../model/match-model';
import { TournamentService } from '../../service/tournament.service';

@Component({
  selector: 'app-tournament-detail',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './tournament-detail.component.html',
  styleUrl: './tournament-detail.component.css'
})
export class TournamentDetailComponent implements OnInit {
  id!: string;
  listMatches: MatchModel[] = [];

  constructor(private tournamentService: TournamentService,  private route: ActivatedRoute){  }
  
  ngOnInit(): void {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.getMatches();
  }

  getMatches(){
    this.tournamentService.getMatches(this.id).subscribe(resp => {
      if(resp){
        this.listMatches = resp;
      }
    })
  }
  startMatch(match_id: string){
    //this.tournamentService.
  }
}
