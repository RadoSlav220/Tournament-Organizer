import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AthleteComponent } from './components/athlete/athlete.component';
import { TeamComponent } from './components/team/team.component';

import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AthleteService } from './service/athlete.service';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { TeamService } from './service/team.service';
import { TournamentComponent } from './components/tournament/tournament.component';
import { TournamentService } from './service/tournament.service';
import { RouterLink, RouterLinkActive, RouterModule, RouterOutlet, provideRouter, withComponentInputBinding } from '@angular/router';
import { AthleteDetailComponent } from './components/athlete-detail/athlete-detail.component';
import { MatchModel } from './model/match-model';

@NgModule({
    declarations: [
        AppComponent
    ],
    providers: [
        provideClientHydration(),
        provideHttpClient(withFetch()),
        AthleteService,
        TeamService,
        TournamentService,
        provideAnimationsAsync(),
        //provideRouter(appRoutes, withComponentInputBinding())
    ],
    bootstrap: [AppComponent],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        AthleteComponent,
        TeamComponent,
        TournamentComponent,
        AthleteDetailComponent,
        CommonModule,
        RouterModule,
        RouterLink, 
        RouterOutlet, 
        RouterLink, 
        RouterLinkActive
    ]
})
export class AppModule { }
