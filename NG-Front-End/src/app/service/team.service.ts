import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TeamModel } from '../model/team-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  private apiUrl;

  constructor(private httpClient: HttpClient) {
    this.apiUrl = 'http://localhost:8081/teams';
  }

  getTeams(): Observable<TeamModel[]>{
    return this.httpClient.get<TeamModel[]>(this.apiUrl);
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
}
