import { Injectable } from '@angular/core';
import {Command} from './Command';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RemoveStoryService extends Command {

  description(): string {
    return 'suprimme une story d\'un sprint';
  }

  exec(cmd: CmdRequestModel) {
     this.http.post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe((e) => alert(e.body), () => alert('erreur lors de la suppression de la story'));
    console.log(cmd);
  }

  identifier(): string {
    return 'remove_story';
  }

  constructor(private http: HttpClient) {
    super();
  }
}
