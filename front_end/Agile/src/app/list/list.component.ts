import { Component, OnInit } from '@angular/core';
import {Tile} from '../grid-list/grid-list.component';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';
import {Sprint} from '../shared/models/Sprint';
import {BackendApiService} from '../shared/services/backend-api.service';


@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  sprints: Sprint[] = [];
  sprint: Sprint;
  constructor(private backendApiService: BackendApiService) { }

  ngOnInit() {
    this.backendApiService.sprints.subscribe(x => this.sprints = x);
    this.backendApiService.getSprints();
  }

  sprintClick(sprint: Sprint) {
    this.sprint = sprint;
    this.backendApiService.changeScope(sprint.name);
  }

}
