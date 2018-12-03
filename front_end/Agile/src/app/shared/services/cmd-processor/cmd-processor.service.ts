import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {URLBACKEND} from '../../constants/urls';
import {Command} from '../commands/Command';
import {ListBacklogService} from '../commands/list-backlog.service';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {RemoveStoryService} from '../commands/remove-story.service';
import {WhatIfIAddService} from '../commands/what-if-iadd.service';
import {WhatIfIRemoveService} from '../commands/what-if-iremove.service';
import {AddStoryService} from '../commands/add-story.service';
import {CreateSprintService} from '../commands/create-sprint.service';
import {InitBacklogService} from '../commands/init-backlog.service';
import {RemoveFromBacklogService} from "../commands/remove-from-backlog.service";

@Injectable({
  providedIn: 'root'
})
export class CmdProcessorService {


  private readonly _cmdOutput$: BehaviorSubject<string>;
  private readonly base_url: string;
  private readonly cmds: Command[];

  constructor(private l: ListBacklogService,
              private rs: RemoveStoryService,
              private w: WhatIfIAddService,
              private wr: WhatIfIRemoveService,
              private as: AddStoryService,
              private cs: CreateSprintService,
              private ib: InitBacklogService,
              private rb: RemoveFromBacklogService) {

    this._cmdOutput$ = new BehaviorSubject<string>('');
    this.base_url = URLBACKEND + '/main';
    this.cmds = [];
    this.addCommand(l);
    this.addCommand(rs);
    this.addCommand(w);
    this.addCommand(wr);
    this.addCommand(as);
    this.addCommand(cs);
    this.addCommand(ib);
    this.addCommand(rb);
  }

  public execCmd(cmd: string, args: string[]) {
    const c = this.cmds.find(x => x.identifier() === cmd);
    if (c === undefined) {
      this.cmdOutput$.next('Unknown command');
      return;
    }
    c.exec(new CmdRequestModel(cmd, args));
  }

  get cmdOutput$(): BehaviorSubject<string> {
    return this._cmdOutput$;
  }


  addCommand(cmd: Command) {
    this.cmds.push(cmd);
    cmd.init(this.cmdOutput$);
  }
}
