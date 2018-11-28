import { Component, OnInit } from '@angular/core';
import {UserStory} from '../shared/models/UserStory';
import {BackendApiService} from '../shared/services/backend-api.service';
import {Sprint} from '../shared/models/Sprint';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {

  backlog: UserStory[];
  sprints: Sprint[] = [];
  private storyToAdd: UserStory;

  constructor(private backendApiService: BackendApiService, private cmdProcessor: CmdProcessorService) { }

  ngOnInit() {
    this.backendApiService.backlog.subscribe(x => this.backlog = x);
    this.backendApiService.loadBacklog();
    this.backendApiService.sprints.subscribe( x  => this.sprints = x);
  }

  onAddStoryMouseEnter(us: UserStory) {
    this.storyToAdd = us;
  }

  onChooseSprintClicked(sp: Sprint) {
    this.cmdProcessor.execCmd('add_story', [sp.name, this.storyToAdd.name]);

  }
}
