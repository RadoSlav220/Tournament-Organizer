import { Component, Input, OnInit } from '@angular/core';
import { AthleteModel } from '../../model/athelete-model';
import { AthleteService } from '../../service/athlete.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';


@Component({
  selector: 'app-athlete',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, RouterOutlet],
  templateUrl: './athlete.component.html',
  styleUrl: './athlete.component.css'
})
export class AthleteComponent implements OnInit {
  listAthletes: AthleteModel[] = [];
  formAthlete: FormGroup = new FormGroup({});
  isUpdate: boolean = false;

constructor(private athleteService: AthleteService, private router: Router){  }
  
  ngOnInit(): void {
    this.list();
    this.formAthlete = new FormGroup({
      'id': new FormControl(''),
      'name': new FormControl(''),
      'age': new FormControl(''),
      'height': new FormControl(''),
      'weight': new FormControl(''),
      'sportType': new FormControl(''),
      'category': new FormControl('')
    });
  }
  
  @Input()
  list(){
    this.athleteService.getAthletes() 
      .subscribe(resp => {
        this.listAthletes = resp;
      });
  }

  newAthlete(){
    this.athleteService.createAthlete(this.formAthlete.value).subscribe(
      resp => {
        if(resp){
          this.listAthletes.push(resp);
          this.formAthlete.reset();
        }
      }
    );
  }

  openNewAthlete(){
    this.formAthlete.reset();
    this.isUpdate = false;
  }

  openUpdateModal(athelete: AthleteModel){
    this.isUpdate = true;
    this.formAthlete.patchValue({
      'id': athelete.id,
      'name': athelete.name,
      'age': athelete.age,
      'height': athelete.height,
      'weight': athelete.weight,
      'sportType': athelete.sportType,
      'category': athelete.category
    });
  }

  update(){
    this.athleteService.updateAthlete(this.formAthlete.value).subscribe(
      resp =>{
        if(resp){
          this.list();
          this.formAthlete.reset();
        }
      }
    );
  }

  detail(id: string){
    this.router.navigate(['/athleteDetail', id]);
  }

  delete(id: string){
    this.athleteService.deleteAthlete(id).subscribe(
      () =>{
        this.listAthletes = this.listAthletes.filter(athlete => athlete.id != id);
      }
    );
  }
}
