import { Component, OnInit } from '@angular/core';
import {UserStory} from '../shared/models/UserStory';
import {BackendApiService} from '../shared/services/backend-api.service';
import {Sprint} from '../shared/models/Sprint';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';
import {MatSelectChange} from "@angular/material";

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
  changedValue(value: MatSelectChange){
    switch (value.value) {
      case "byName":
        console.log("byName");
        break;
      case "byValue":
        console.log("byValue");
        break;
      case "byComplexity":
        console.log("byComplexity");
        break;
      default:
        console.log("Not implemented yet");
        break;
    }
  }

}
