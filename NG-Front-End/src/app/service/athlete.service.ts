import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map, pipe } from 'rxjs';
import { AthleteModel } from '../model/athelete-model';

@Injectable({
  providedIn: 'root'
})
export class AthleteService {
  private apiUrl;

  constructor(private httpClient: HttpClient) {
    this.apiUrl = 'http://localhost:8081/athletes';
   }

   getAthletes(): Observable<AthleteModel[]>{
    return this.httpClient.get<AthleteModel[]>(this.apiUrl);
   }

   createAthlete(athlete: AthleteModel): Observable<any>{
    return this.httpClient.post(this.apiUrl, athlete);
   }

   updateAthlete(athlete: AthleteModel): Observable<any>{
    return this.httpClient.put<any>(this.apiUrl + '/' + athlete.id, athlete);
   }

   deleteAthlete(id: string): Observable<any>{
    return this.httpClient.delete<any>(this.apiUrl + '/' + id);
   }
}
