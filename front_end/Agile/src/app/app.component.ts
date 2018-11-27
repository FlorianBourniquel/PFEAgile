import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor() {
    window['comp'] = {component : this};
  }

  public nodeClick(node) {
    alert(JSON.stringify({ data: node}, null, 4));
  }
  title = 'Agile';
}


