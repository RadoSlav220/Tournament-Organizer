import { HttpClient, HttpHeaders } from '@angular/common/http';
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
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.get<TournamentModel[]>(this.apiUrl,{ headers });
  }

   createTournament(tournament: TournamentModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.post(this.apiUrl, tournament, { headers });
   }

   updateTournament(tournament: TournamentModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.put<any>(this.apiUrl + '/' + tournament.id, tournament, { headers });
   }

   deleteTournament(id: string): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.delete<any>(this.apiUrl + '/' + id, { headers });
   }

   startTournament(tournament: TournamentModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });
    
      return this.httpClient.post<any>(this.apiUrl + '/' + tournament.id + '/start', tournament, { headers });
   }
}
