import { Injectable } from '@angular/core';
import {Command} from './Command';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {UserStory} from '../../models/UserStory';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class ListBacklogService extends Command {

  constructor(private http: HttpClient) {
    super();
  }

  description(): string {
    return 'liste le backlog backlog';
  }

  exec(cmd: CmdRequestModel) {

    this.http
      .post<UserStory[]>(this.base_url, cmd, { observe: 'response', responseType: 'json'})
      .subscribe(resp => this.printResponse(resp.body), error => this.handleError(error));
  }

  printResponse(stories: UserStory[]) {
    const output = stories.map(s => '[' + s.name + '] ' + s.text).join('<br>');
    this.displayOutput(output);
  }

  identifier(): string {
    return 'list_backlog';
  }



}
