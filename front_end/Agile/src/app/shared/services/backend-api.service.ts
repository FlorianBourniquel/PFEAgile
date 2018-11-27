import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {URLBACKEND} from '../constants/urls';
import {CmdRequestModel} from '../models/CmdRequestModel';
import {UserStory} from '../models/UserStory';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BackendApiService {

  private base_url: string;

  constructor(private http: HttpClient) {
    this.base_url = URLBACKEND + '/main';
  }

  public listBacklog() {
    const data = [];
   // this.http.post<UserStory>(this.base_url, data, { observe: 'response', responseType: 'text'});
  }

  public by() {
    const data = new CmdRequestModel('bye', []);
    this.http.post(this.base_url, data, { observe: 'response', responseType: 'text'})
      .subscribe(x => console.log(x.body), error => console.log(error.error));
  }
}
