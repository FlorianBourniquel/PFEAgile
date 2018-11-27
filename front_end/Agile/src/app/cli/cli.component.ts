import { Component, OnInit } from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {UserStory} from '../shared/models/UserStory';
import {BackendApiService} from '../shared/services/backend-api.service';

@Component({
  selector: 'app-cli',
  templateUrl: './cli.component.html',
  styleUrls: ['./cli.component.css']
})
export class CliComponent implements OnInit {

  public response = 'yo';
  constructor(private backendApi: BackendApiService) { }

  ngOnInit() {
  }

  public onSubmit(e: Event) {
    e.preventDefault();
    this.backendApi.listBacklog().subscribe( (c: HttpResponse<UserStory[]>) => {
      this.response = c.body.map( x => x.name ).join(' \n');
    });
  }
}
