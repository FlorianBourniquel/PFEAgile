import { Component, OnInit } from '@angular/core';
import {UserStory} from '../shared/models/UserStory';
import {BackendApiService} from '../shared/services/backend-api.service';
import {Sprint} from '../shared/models/Sprint';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';
import {MatSelectChange} from "@angular/material";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {

  disableSelect = new FormControl(true);
  backlog: UserStory[];
  sprints: Sprint[] = [];
  private storyToAdd: UserStory;
  private wantedFilter: string;
  selected: string;

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

  onVisualiseImpactSprintClicked(sp: Sprint) {
    this.backendApiService.visualiseImpact( this.storyToAdd.name, sp.name, true);
  }

  selectedSprint(value: MatSelectChange){
    if(this.wantedFilter == "byComplexity"){
      this.backendApiService.loadBacklogByComplexity(value.value);
    }
  }

  changedValue(value: MatSelectChange){
    this.disableSelect.setValue(true);
    this.wantedFilter = value.value;

    switch (value.value) {
      case "byName":
        this.backendApiService.loadBacklog();
        break;
      case "byValue":
        this.backendApiService.loadBacklogByValue();
        console.log("byValue");
        break;
      case "byComplexity":
        this.disableSelect.setValue(false);
        break;
      default:
        console.log("Not implemented");
        break;
    }
  }

}
