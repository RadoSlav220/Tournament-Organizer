import { HttpClient, HttpHeaders } from '@angular/common/http';
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
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.get<AthleteModel[]>(this.apiUrl, { headers });
   }

   getAthleteById(id: string): Observable<AthleteModel>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.get<AthleteModel>(this.apiUrl + '/' + id, { headers });
  }

  getMatches(id: string): Observable<MatchModel[]> {
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.get<MatchModel[]>(this.apiUrl + '/matches/' + id, { headers });
  }

   createAthlete(athlete: AthleteModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.post(this.apiUrl, athlete, { headers });
   }

   updateAthlete(athlete: AthleteModel): Observable<AthleteModel>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.put<any>(this.apiUrl + '/' + athlete.id, athlete, { headers });
   }

   deleteAthlete(id: string): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.delete<any>(this.apiUrl + '/' + id, { headers });
   }

   getTournamentForRegister(id: string): Observable<TournamentModel[]>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
   
    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/' + id, { headers });
   }

   register(athlete: AthleteModel, tournament_id: string): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.post<any>(this.apiUrl2 + '/' + athlete.id + '/' + tournament_id, athlete, { headers });
   }

   getTournamentForUnregister(id: string): Observable<TournamentModel[]>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/un/' + id, { headers });
   }

   unregister(athlete: AthleteModel, tournament_id: string): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
    return this.httpClient.post<any>(this.apiUrl2 + '/un/' + athlete.id + '/' + tournament_id, athlete, { headers });
   }
}
