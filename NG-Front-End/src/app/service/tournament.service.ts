import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TournamentModel } from '../model/tournament-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TournamentService {
  private apiUrl;

  constructor(private httpClient: HttpClient) {
    this.apiUrl = 'http://localhost:8081/knockOutTournament';
  }

  getTournaments(): Observable<TournamentModel[]>{
    return this.httpClient.get<TournamentModel[]>(this.apiUrl);
    }

   createTournament(tournament: TournamentModel): Observable<any>{
    return this.httpClient.post(this.apiUrl, tournament);
   }

   updateTournament(tournament: TournamentModel): Observable<any>{
    return this.httpClient.put<any>(this.apiUrl + '/' + tournament.id, tournament);
   }

   deleteTournament(id: string): Observable<any>{
    return this.httpClient.delete<any>(this.apiUrl + '/' + id);
   }

   startTournament(tournament: TournamentModel): Observable<any>{
      return this.httpClient.post<any>(this.apiUrl + '/' + tournament.id + '/start', tournament);
   }
}
