import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {BackendApiService} from './shared/services/backend-api.service';
import {Class} from './shared/models/Class';
import {CmdProcessorService} from './shared/services/cmd-processor/cmd-processor.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {

  @Input() title;
  cd: ChangeDetectorRef;

  constructor(private backendApiService: BackendApiService, cd: ChangeDetectorRef, private cmdProcessor: CmdProcessorService) {
    window['comp'] = {component: this};
    this.title = 'Agile';
    this.cd = cd;
  }

  public nodeClick(node) {
    // alert(JSON.stringify({ data: node}, null, 4));
    if (node.title === 'CLASS') {
      console.log(node);
      const c = new Class();
      c.name = node.id;
      this.cmdProcessor.execCmd('list_stories_involving_class', [c.name]);
    }
  }

  ngOnInit(): void {
    this.backendApiService.scope
      .subscribe(x => document.getElementById('graphe')
      .setAttribute('src', 'assets/graphs/network.html?' + Math.random().toString(10).substring(2)));
  }
}


