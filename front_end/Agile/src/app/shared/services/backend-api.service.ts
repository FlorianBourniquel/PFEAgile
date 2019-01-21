import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {URLBACKEND} from '../constants/urls';
import {CmdRequestModel} from '../models/CmdRequestModel';
import {BehaviorSubject} from 'rxjs';
import {UserStory} from '../models/UserStory';
import {Sprint} from '../models/Sprint';
import {StoryWithCompexity} from '../models/StoryWithCompexity';

@Injectable({
  providedIn: 'root'
})
export class BackendApiService {

  private base_url: string;
  private _sprints: BehaviorSubject <Sprint[]>;
  private _backlog: BehaviorSubject <UserStory[]>;
  private _scope: BehaviorSubject <Sprint>;

  constructor(private http: HttpClient) {
    this.base_url = URLBACKEND + '/main';
    this._sprints = new BehaviorSubject([]);
    this._backlog = new BehaviorSubject([]);
    this._scope = new BehaviorSubject(undefined);
  }

  loadBacklogByComplexity(sprintName: string) {
    const data = new CmdRequestModel('sort_backlog_by_complexity', [sprintName]);
    this.http.post<StoryWithCompexity[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => {
        const stories = x.body.map( e => {
          const s = e.userStory;
          s.complexity = new Map<string, number>();
          s.complexity.set('nbClassesAdded', e.nbClassesAdded);
          return s;
        });
        this.backlog.next(stories);
        },
        error1 => console.log(error1));
  }

  public loadBacklog(): void {
    const data = new CmdRequestModel('list_backlog', []);
    this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => { this.backlog.next(x.body); });
  }

  public loadBacklogByValue(): void {
    const data = new CmdRequestModel('sort_backlog_by_value', []);
    this.http.post<UserStory[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe(x => {
        const res = x.body;
        res.forEach(us => us.displayRatio = true);
        this.backlog.next(x.body);
      });
  }

  public by() {
    const data = new CmdRequestModel('bye', []);
    this.http.post(this.base_url, data, { observe: 'response', responseType: 'text'})
      .subscribe(x => console.log(x.body), error => console.log(error.error));
  }

  public loadSprints(): void {
    const data = new CmdRequestModel('list_sprint', []);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { this.sprints.next(x.body); });

  }

  get sprints(): BehaviorSubject<Sprint[]> {
    return this._sprints;
  }


  get scope(): BehaviorSubject<Sprint> {
    return this._scope;
  }


  get backlog(): BehaviorSubject<UserStory[]> {
    return this._backlog;
  }

  public changeScope(s): void {
    const data = new CmdRequestModel('visualise_domain_sprint', [s.name]);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next(s); console.log(this.scope.getValue()); });

  }

  public changeScopeAll(): void {
    const data = new CmdRequestModel('visualise_domain', []);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next(undefined); });
  }

  public visualiseImpact( sprint, us, mode): void {
    const data = new CmdRequestModel('visualise_impact', [sprint.name, mode, us]);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next(sprint); });
  }

  visualiseImpactNextSprint(value: Sprint, name: string) {
    const data = new CmdRequestModel('visualise_impact_next_sprint', [value.name, name]);
    this.http.post<Sprint[]>(this.base_url, data, { observe: 'response', responseType: 'json'})
      .subscribe( x => { console.log(x.body); this.scope.next(value);});
  }
}
