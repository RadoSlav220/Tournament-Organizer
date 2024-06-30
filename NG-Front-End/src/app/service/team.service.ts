import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TeamModel } from '../model/team-model';
import { Observable } from 'rxjs';
import { MatchModel } from '../model/match-model';
import { TournamentModel } from '../model/tournament-model';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private apiUrl;
  private apiUrl2;

  constructor(private httpClient: HttpClient) {
    this.apiUrl = 'http://localhost:8081/teams';
    this.apiUrl2 = 'http://localhost:8081/registration'
  }

  getTeams(): Observable<TeamModel[]>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.get<TeamModel[]>(this.apiUrl, { headers });
  }

  getTeamById(id: string): Observable<TeamModel>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.get<TeamModel>(this.apiUrl + '/' + id, { headers });
  }

  getMatches(id: string): Observable<MatchModel[]> {
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.get<MatchModel[]>(this.apiUrl + '/matches/' + id, { headers });
  }

   createTeam(team: TeamModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    team.players = team.players.toString().split(',');

    return this.httpClient.post(this.apiUrl, team, { headers });
   }

   updateTeam(team: TeamModel): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    team.players = team.players.toString().split(',');

    return this.httpClient.put<any>(this.apiUrl + '/' + team.id, team, { headers });
   }

   deleteTeam(id: string): Observable<any>{
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

   register(athlete: TeamModel, tournament_id: string): Observable<any>{
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

   unregister(athlete: TeamModel, tournament_id: string): Observable<any>{
    const credentials = btoa(`admin:1234`);
    const headers = new HttpHeaders({
      'Authorization': `Basic ${credentials}`
    });

    return this.httpClient.post<any>(this.apiUrl2 + '/un/' + athlete.id + '/' + tournament_id, athlete, { headers });
   }
}
