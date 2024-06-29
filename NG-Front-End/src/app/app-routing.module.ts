import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AthleteComponent } from './components/athlete/athlete.component';
import { AthleteDetailComponent } from './components/athlete-detail/athlete-detail.component';
import { CommonModule } from '@angular/common';
import { TeamComponent } from './components/team/team.component';
import { TeamDetailComponent } from './components/team-detail/team-detail.component';
import { TournamentComponent } from './components/tournament/tournament.component';
import { TournamentDetailComponent } from './components/tournament-detail/tournament-detail.component';
import { AppComponent } from './app.component';

export const routes: Routes = [
  {path: 'athletes', component: AthleteComponent},
  {path: 'athleteDetail/:id', component: AthleteDetailComponent},
  {path: 'teams', component: TeamComponent},
  {path: 'teamDetail/:id', component: TeamDetailComponent},
  {path: 'tournaments', component: TournamentComponent},
  {path: 'tournamentDetail/:id', component: TournamentDetailComponent}
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
