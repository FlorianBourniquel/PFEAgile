import { Injectable } from '@angular/core';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {HttpClient} from '@angular/common/http';
import {Command} from './Command';
import {BackendApiService} from '../backend-api.service';

@Injectable({
  providedIn: 'root'
})
export class InitBacklogService extends Command {

  description(): string {
    return 'Initialiser le backlog';
  }

  exec(cmd: CmdRequestModel) {
    this.http.post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe((e) => { alert(e.body); this.backendApiService.loadBacklog(); this.backendApiService.loadSprints(); } ,
                 () => alert('erreur lors de l\'initialisation du backlog'));
  }

  identifier(): string {
    return 'init_backlog';
  }

  constructor(private http: HttpClient, private backendApiService: BackendApiService) {
    super();
  }
}
