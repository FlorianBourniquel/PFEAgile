import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {UserStory} from '../../models/UserStory';
import {Command} from './Command';
import {Sprint} from '../../models/Sprint';

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
     const data = new CmdRequestModel('list_sprint', []);
     this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
          .subscribe( x => {
              let sprints = x.body;
              let notInBacklog = [];
              sprints.forEach( sp => sp.storyList = sp.storyList.filter( st => this.storyIn(st,stories, notInBacklog)));
              const backlog: Sprint = {
                  name: 'backlog',
                  storyList: []
              };
              backlog.storyList = stories.filter( st => ! this.storyIn2(st, notInBacklog));
              sprints.push(backlog);

              sprints = sprints.filter(sp => sp.storyList.length > 0);
              const output = sprints.map(s => this.sprintToString(s)).join('\n');
              alert(output);
          });
  }

   private sprintToString(sprint: Sprint): string {
        let s = sprint.name + '\n';
            s = s + sprint.storyList.map(us => ' [' + us.name + '] ' + us.text).join('\n');
        return s;
   }

   private storyIn2(story: UserStory, list: UserStory[]): boolean {
        return  list.find(value => value.name === story.name) !== undefined;
    }

  private storyIn(story: UserStory, list: UserStory[], notInbacklog: UserStory[]): boolean {
      let res =  list.find(value => value.name === story.name) !== undefined;
      if(res === true) {
          notInbacklog.push(story);
      }
      return res;
  }

  identifier(): string {
    return 'list_stories_involving_class';
  }

}
