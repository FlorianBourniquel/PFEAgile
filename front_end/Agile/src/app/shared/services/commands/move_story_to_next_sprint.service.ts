import { Injectable } from '@angular/core';
import {Command} from './Command';
import {HttpClient} from '@angular/common/http';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {BackendApiService} from '../backend-api.service';

@Injectable({
  providedIn: 'root'
})
export class MoveStoryToNextSprintServiceService  extends Command {

  description(): string {
    return 'move a story to the next sprint';
  }

  exec(cmd: CmdRequestModel) {
    this.http
      .post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe(
        resp => {alert(resp.body); this.backendApiService.loadBacklog(); this.backendApiService.loadSprints(); },
       error => this.handleError(error));
  }

  identifier(): string {
    return 'move_story_to_next_sprint';
  }


  constructor(private http: HttpClient, private backendApiService: BackendApiService) {
    super();
  }
}
