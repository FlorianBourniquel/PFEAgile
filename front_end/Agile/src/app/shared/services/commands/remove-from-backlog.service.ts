import { Injectable } from '@angular/core';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {HttpClient} from '@angular/common/http';
import {Command} from './Command';
import {BackendApiService} from '../backend-api.service';

@Injectable({
  providedIn: 'root'
})
export class RemoveFromBacklogService extends Command {

  description(): string {
    return 'Supprime du backlog une histoire utilisateur';
  }

  exec(cmd: CmdRequestModel) {
    this.http.post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe((e) => {
      alert(e.body); this.backendApiService.loadBacklog() } ,
        (error => this.handleError(error)));
  }

  identifier(): string {
    return 'remove_from_backlog';
  }

  constructor(private http: HttpClient, private backendApiService: BackendApiService) {
    super();
  }
}
