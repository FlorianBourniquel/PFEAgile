import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {BackendApiService} from './shared/services/backend-api.service';
import {HttpResponse} from '@angular/common/http';
import {UserStory} from './shared/models/UserStory';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  @Input() title;
  cd: ChangeDetectorRef;

  constructor(cd: ChangeDetectorRef, private backendApi: BackendApiService) {
    window['comp'] = {component: this};
    this.title = 'Agile';
    this.cd = cd;
  }

  public nodeClick(node) {
    alert(JSON.stringify({ data: node}, null, 4));
  }

  ngOnInit(): void {}
}


