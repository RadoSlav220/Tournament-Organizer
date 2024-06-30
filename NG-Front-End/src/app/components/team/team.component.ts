import { CommonModule } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { TeamModel } from '../../model/team-model';
import { TeamService } from '../../service/team.service';
import { Router, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-team',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './team.component.html',
  styleUrl: './team.component.css'
})
export class TeamComponent implements OnInit {
  listTeams: TeamModel[] = [];
  formTeam: FormGroup = new FormGroup({});
  isUpdate: boolean = false;

  constructor(private teamService: TeamService, private router: Router){  }
  
  ngOnInit(): void {
    this.list();
    this.formTeam = new FormGroup({
      'id': new FormControl(''),
      'name': new FormControl(''),
      'sportType': new FormControl(''),
      'category': new FormControl(''),
      'manager': new FormControl(''),
      'establishmentYear': new FormControl(''),
      'players': new FormControl(''),
    });
  }

  @Input()
  list(){
    this.teamService.getTeams()
      .subscribe(resp => {
        this.listTeams = resp;
      })
  }

  newTeam(){
    this.teamService.createTeam(this.formTeam.value).subscribe(
      resp => {
        if(resp){
          this.listTeams.push(resp);
          this.formTeam.reset();
        }
      }
    );
  }

  openNewTeam(){
    this.formTeam.reset();
    this.isUpdate = false;
  }

  openUpdateModal(team: TeamModel){
    this.isUpdate = true;
    this.formTeam.patchValue({
      'id': team.id,
      'name': team.name,
      'sportType': team.sportType,
      'category': team.category,
      'manager': team.manager,
      'establishmentYear': team.establishmentYear,
      'players': team.players
    });
  }

  update(){
    this.teamService.updateTeam(this.formTeam.value).subscribe(
      resp =>{
        if(resp){
          this.list();
          this.formTeam.reset();
        }
      }
    );
  }

  detail(id: string){
    this.router.navigate(['/teamDetail', id]);
  }

  delete(id: string){
    this.teamService.deleteTeam(id).subscribe(
      () =>{
        this.listTeams = this.listTeams.filter(team => team.id != id);
      }
    );
  }
  
}
