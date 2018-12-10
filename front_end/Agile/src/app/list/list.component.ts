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
  showButton: boolean = false;
  count: number = 0;
  toMoveUS: UserStory;
  constructor(private backendApiService: BackendApiService, private cmdProcessor: CmdProcessorService) { }

  ngOnInit() {
    this.backendApiService.sprints.subscribe(x => this.sprints = x);
    this.backendApiService.loadSprints();
  }

  sprintClick(sprint: Sprint) {
    this.backendApiService.changeScope(sprint);
    this.hideButton();
  }

  allDomain() {
    this.backendApiService.changeScopeAll();
    this.hideButton();
  }

  onRemoveUsClicked(us: UserStory) {
    this.cmdProcessor.execCmd('remove_story', [this.backendApiService.scope.getValue().name, us.name]);
  }

  onClickVisualiseImpact(us: UserStory) {
    this.backendApiService.visualiseImpact( this.backendApiService.scope.getValue() , us.name, false);
  }

  onClickedMoveStoryToNextSprint(us: UserStory) {
    this.showButton = true;
    this.toMoveUS = us;
  }

  dismissPostpone() {
    this.allDomain();
    this.hideButton();
  }

  confirmPostpone() {
    if (this.count == 0) {
      this.backendApiService.visualiseImpact( this.backendApiService.scope.getValue() , this.toMoveUS.name, false);
    } else if(this.count == 1) {
      this.backendApiService.visualiseImpactNextSprint( this.backendApiService.scope.getValue() , this.toMoveUS.name);
    } else {
      this.hideButton();
      this.cmdProcessor.execCmd('move_story_to_next_sprint', [this.backendApiService.scope.getValue().name, this.toMoveUS.name]);
    }

    this.count++;
  }

  hideButton(){
    this.showButton = false;
    this.count = 0;
  }
}
