import { Component, OnInit } from '@angular/core';
import {UserStory} from '../shared/models/UserStory';
import {BackendApiService} from '../shared/services/backend-api.service';

@Component({
  selector: 'app-backlog',
  templateUrl: './backlog.component.html',
  styleUrls: ['./backlog.component.css']
})
export class BacklogComponent implements OnInit {

  backlog: UserStory[];
  constructor(private backendApiService: BackendApiService) { }

  ngOnInit() {
    this.backendApiService.backlog.subscribe(x => this.backlog = x);
    this.backendApiService.loadBacklog();
  }

}
