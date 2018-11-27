import {Component, OnInit} from '@angular/core';
import {BackendApiService} from './shared/services/backend-api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  constructor(private backendApi: BackendApiService) {
    window['comp'] = {component : this};
  }

  N

  public nodeClick(node) {
    alert(JSON.stringify({ data: node}, null, 4));
  }
  title = 'Agile';

  ngOnInit(): void {
    this.backendApi.by();
  }
}


