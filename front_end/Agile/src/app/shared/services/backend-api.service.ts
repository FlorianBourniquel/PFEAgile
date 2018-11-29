import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {URLBACKEND} from '../constants/urls';
import {CmdRequestModel} from '../models/CmdRequestModel';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserStory} from '../models/UserStory';
import {Sprint} from '../models/Sprint';

@Injectable({
  providedIn: 'root'
})
export class BackendApiService {

  private base_url: string;
  private _sprints: BehaviorSubject <Sprint[]>;
  private _backlog: BehaviorSubject <UserStory[]>;
  private _scope: BehaviorSubject <String>;

  constructor(private http: HttpClient) {
    this.base_url = URLBACKEND + '/main';
    this._sprints = new BehaviorSubject([]);
    this._backlog = new BehaviorSubject([]);
    this._scope = new BehaviorSubject('');
  }

  loadBacklogByComplexity(sprintName: string) {
    const data = new CmdRequestModel('sort_backlog_by_complexity', [sprintName]);
    this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => {console.log(x.body); this.backlog.next(x.body); });
  }

  public loadBacklog(): void {
    const data = new CmdRequestModel('list_backlog', []);
    this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => {console.log(x.body); this.backlog.next(x.body); });
  }

  public loadBacklogByValue(): void {
    const data = new CmdRequestModel('sort_backlog_by_value', []);
    this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => {console.log(x.body); this.backlog.next(x.body); });
  }

  public by() {
    const data = new CmdRequestModel('bye', []);
    this.http.post(this.base_url, data, { observe: 'response', responseType: 'text'})
      .subscribe(x => console.log(x.body), error => console.log(error.error));
  }

  public loadSprints(): void {
    const data = new CmdRequestModel('list_sprint', []);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.sprints.next(x.body); });

  }

  get sprints(): BehaviorSubject<Sprint[]> {
    return this._sprints;
  }


  get scope(): BehaviorSubject<String> {
    return this._scope;
  }


  get backlog(): BehaviorSubject<UserStory[]> {
    return this._backlog;
  }

  public changeScope(s): void {
    const data = new CmdRequestModel('visualise_domain_sprint', [s]);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next('d'); });

  }

  public changeScopeAll(): void {
    const data = new CmdRequestModel('visualise_domain', []);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next('d'); });
  }

  public visualiseImpact(us, sprint, mode): void {
    const data = new CmdRequestModel('visualise_impact', [sprint, mode, us]);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next('d'); });
  }
}
