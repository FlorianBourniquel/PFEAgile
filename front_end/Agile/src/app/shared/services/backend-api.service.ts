import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {URLBACKEND} from '../constants/urls';
import {CmdRequestModel} from '../models/CmdRequestModel';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserStory} from '../models/UserStory';
import {Sprint} from "../models/Sprint";

@Injectable({
  providedIn: 'root'
})
export class BackendApiService {

  private base_url: string;
  private _sprints: BehaviorSubject <Sprint[]>;

  constructor(private http: HttpClient) {
    this.base_url = URLBACKEND + '/main';
    this._sprints = new BehaviorSubject([]);
  }

  public listBacklog(): Observable<HttpResponse<UserStory[]>> {
    const data = new CmdRequestModel('list_backlog', []);
    return this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'});
  }

  public by() {
    const data = new CmdRequestModel('bye', []);
    this.http.post(this.base_url, data, { observe: 'response', responseType: 'text'})
      .subscribe(x => console.log(x.body), error => console.log(error.error));
  }

  public getSprints(): void {
    const data = new CmdRequestModel('list_sprint', []);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => this.sprints.next(x.body));
  }

  get sprints(): BehaviorSubject<Sprint[]> {
    return this._sprints;
  }
}
