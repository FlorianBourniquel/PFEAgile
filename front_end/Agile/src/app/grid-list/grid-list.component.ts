import {Component, NgModule} from '@angular/core';
import {AppComponent} from '../app.component';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MaterialModule} from '../material/material-module';
import {AppRoutingModule} from '../app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material';
import {HttpClientModule} from '@angular/common/http';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: number;
}

/**
 * @title Dynamic grid-list
 */
@Component({
  selector: 'app-grid-list',
  templateUrl: './grid-list.component.html',
  styleUrls: ['./grid-list.component.css']
})
@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    MaterialModule,
    MatNativeDateModule,
    ReactiveFormsModule,
  ],
  entryComponents: [GridListComponent],
  declarations: [GridListComponent],
  bootstrap: [GridListComponent],
  providers: []
})

export class GridListComponent {
  tiles: Tile[] = [
    {text: 1, cols: 3, rows: 2, color: 'lightblue'},
    {text: 2, cols: 1, rows: 2, color: 'lightgreen'}
  ];
}
