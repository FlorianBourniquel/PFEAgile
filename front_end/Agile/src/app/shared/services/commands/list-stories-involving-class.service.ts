import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {UserStory} from '../../models/UserStory';
import {Command} from './Command';

@Injectable({
  providedIn: 'root'
})
export class ListStoriesInvolvingClassService extends Command {

  constructor(private http: HttpClient) {
    super();
  }

  description(): string {
    return 'list stories involing class';
  }

  exec(cmd: CmdRequestModel) {
    this.http
      .post<UserStory[]>(this.base_url, cmd, { observe: 'response', responseType: 'json'})
      .subscribe(resp => this.printResponse(resp.body), error => this.handleError(error));
  }

  printResponse(stories: UserStory[]) {
    const output = stories.map(s => '[' + s.name + '] ' + s.text).join('\n');
    alert(output);
  }

  identifier(): string {
    return 'list_stories_involving_class';
  }

}
