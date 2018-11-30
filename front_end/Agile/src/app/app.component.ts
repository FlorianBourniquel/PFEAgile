import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {BackendApiService} from './shared/services/backend-api.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  @Input() title;
  cd: ChangeDetectorRef;

  constructor(private backendApiService: BackendApiService, cd: ChangeDetectorRef) {
    window['comp'] = {component: this};
    this.title = 'Agile';
    this.cd = cd;
  }

  public nodeClick(node) {
    alert(JSON.stringify({ data: node}, null, 4));
  }

  ngOnInit(): void {
    this.backendApiService.scope.subscribe(x => document.getElementById('graphe').setAttribute('src', 'assets/graphs/network.html?' + Math.random().toString(10).substring(2)));
  }
}


