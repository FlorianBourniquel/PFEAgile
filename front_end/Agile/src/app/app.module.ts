import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from "./material/material-module";


import { GridListComponent } from './grid-list/grid-list.component';
import { CliComponent } from './cli/cli.component';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from "@angular/forms";
import { ListComponent } from './list/list.component';

@NgModule({
  declarations: [
    AppComponent,
    GridListComponent,
    CliComponent,
    ListComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    FormsModule,
    AppRoutingModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  bootstrap: [AppComponent],
  providers: []
})
export class AppModule { }
