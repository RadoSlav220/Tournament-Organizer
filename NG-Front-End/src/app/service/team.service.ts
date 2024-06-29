import { HttpClient } from '@angular/common/http';
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
    return this.httpClient.get<TeamModel[]>(this.apiUrl);
  }

  getTeamById(id: string): Observable<TeamModel>{
    return this.httpClient.get<TeamModel>(this.apiUrl + '/' + id);
  }

  getMatches(id: string): Observable<MatchModel[]> {
    return this.httpClient.get<MatchModel[]>(this.apiUrl + '/matches/' + id);
  }

   createTeam(team: TeamModel): Observable<any>{
    team.players = team.players.toString().split(',');
    return this.httpClient.post(this.apiUrl, team);
   }

   updateTeam(team: TeamModel): Observable<any>{
    team.players = team.players.toString().split(',');
    return this.httpClient.put<any>(this.apiUrl + '/' + team.id, team);
   }

   deleteTeam(id: string): Observable<any>{
    return this.httpClient.delete<any>(this.apiUrl + '/' + id);
   }

   getTournamentForRegister(id: string): Observable<TournamentModel[]>{
    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/' + id);
   }

   register(athlete: TeamModel, tournament_id: string): Observable<any>{
    return this.httpClient.post<any>(this.apiUrl2 + '/' + athlete.id + '/' + tournament_id, athlete);
   }

   getTournamentForUnregister(id: string): Observable<TournamentModel[]>{
    return this.httpClient.get<TournamentModel[]>(this.apiUrl2 + '/un/' + id);
   }

   unregister(athlete: TeamModel, tournament_id: string): Observable<any>{
    return this.httpClient.post<any>(this.apiUrl2 + '/un/' + athlete.id + '/' + tournament_id, athlete);
   }
}
