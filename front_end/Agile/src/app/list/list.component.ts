import { Component, OnInit } from '@angular/core';
import {Sprint} from '../shared/models/Sprint';
import {BackendApiService} from '../shared/services/backend-api.service';
import {UserStory} from '../shared/models/UserStory';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';


@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  sprints: Sprint[] = [];
  constructor(private backendApiService: BackendApiService, private cmdProcessor: CmdProcessorService) { }

  ngOnInit() {
    this.backendApiService.sprints.subscribe(x => this.sprints = x);
    this.backendApiService.loadSprints();
  }

  sprintClick(sprint: Sprint) {
    this.backendApiService.changeScope(sprint);
  }

  allDomain() {
    this.backendApiService.changeScopeAll();
  }

  onRemoveUsClicked(us: UserStory) {
    this.cmdProcessor.execCmd('remove_story', [this.backendApiService.scope.getValue().name, us.name]);
  }

  onClickVisualiseImpact(us: UserStory) {
    this.backendApiService.visualiseImpact( this.backendApiService.scope.getValue() , us.name, false);
  }

  onClickedMoveStoryToNextSprint(us: UserStory) {
    this.cmdProcessor.execCmd('move_story_to_next_sprint', [this.backendApiService.scope.getValue().name, us.name]);
  }
}
