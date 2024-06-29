import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AthleteComponent } from './components/athlete/athlete.component';
import { AthleteDetailComponent } from './components/athlete-detail/athlete-detail.component';
import { CommonModule } from '@angular/common';

export const routes: Routes = [
  {path: 'athletes', component: AthleteComponent},
  {path: 'athleteDetail/:id', component: AthleteDetailComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
