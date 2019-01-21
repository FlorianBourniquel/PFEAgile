import { Injectable } from '@angular/core';
import {Command} from './Command';
import {HttpClient} from '@angular/common/http';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {BackendApiService} from '../backend-api.service';

@Injectable({
  providedIn: 'root'
})
export class CreateSprintService  extends Command {

  description(): string {
    return 'create un sprint';
  }

  exec(cmd: CmdRequestModel) {
    this.http
      .post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe(
        resp => {alert(resp.body); this.backendApiService.loadBacklog(); this.backendApiService.loadSprints(); },
       error => this.handleError(error));
  }

  identifier(): string {
    return 'create_sprint';
  }


  constructor(private http: HttpClient, private backendApiService: BackendApiService) {
    super();
  }
}
