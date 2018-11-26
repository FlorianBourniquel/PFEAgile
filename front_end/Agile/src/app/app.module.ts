import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MaterialModule} from "./material/material-module";


import { GridListComponent } from './grid-list/grid-list.component';
import { TestComponent } from './test/test.component';

@NgModule({
  declarations: [
    AppComponent,
    GridListComponent,
    TestComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule
  ],
  bootstrap: [AppComponent],
  providers: []
})
export class AppModule { }
