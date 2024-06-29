import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, pipe } from 'rxjs';
import { AthleteModel } from '../model/athelete-model';
import { TournamentModel } from '../model/tournament-model';
import { MatchModel } from '../model/match-model';

@Injectable({
  providedIn: 'root'
})
export class AthleteService {
  private apiUrl;
  private apiUrl2;

  constructor(private httpClient: HttpClient) {
    this.apiUrl = 'http://localhost:8081/athletes';
    this.apiUrl2 = 'http://localhost:8081/registration';
   }

   getAthletes(): Observable<AthleteModel[]>{
    return this.httpClient.get<AthleteModel[]>(this.apiUrl);
   }

   getAthleteById(id: string): Observable<AthleteModel>{
    return this.httpClient.get<AthleteModel>(this.apiUrl + '/' + id);
  }

  getMatches(id: string): Observable<MatchModel[]> {
    return this.httpClient.get<MatchModel[]>(this.apiUrl + '/matches/' + id);
  }

   createAthlete(athlete: AthleteModel): Observable<any>{
    return this.httpClient.post(this.apiUrl, athlete);
   }

   updateAthlete(athlete: AthleteModel): Observable<AthleteModel>{
    return this.httpClient.put<any>(this.apiUrl + '/' + athlete.id, athlete);
   }

   deleteAthlete(id: string): Observable<any>{
    return this.httpClient.delete<any>(this.apiUrl + '/' + id);
   }

   getTournamentForRegister(id: string): Observable<TournamentModel[]>{
    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/' + id);
   }

   register(athlete: AthleteModel, tournament_id: string): Observable<any>{
    return this.httpClient.post<any>(this.apiUrl2 + '/' + athlete.id + '/' + tournament_id, athlete);
   }

   getTournamentForUnregister(id: string): Observable<TournamentModel[]>{
    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/un/' + id);
   }

   unregister(athlete: AthleteModel, tournament_id: string): Observable<any>{
    return this.httpClient.post<any>(this.apiUrl2 + '/un/' + athlete.id + '/' + tournament_id, athlete);
   }
}
