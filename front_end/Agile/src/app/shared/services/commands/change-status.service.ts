import {Injectable} from '@angular/core';
import {Command} from './Command';
import {HttpClient} from '@angular/common/http';
import {CmdRequestModel} from '../../models/CmdRequestModel';

@Injectable({
  providedIn: 'root'
})
export class ChangeStatusService  extends Command {

  description(): string {
    return 'change a class status';
  }

  exec(cmd: CmdRequestModel) {
    this.http
      .post(this.base_url, cmd, { observe: 'response', responseType: 'text'})
      .subscribe(
        resp => {alert(resp.body);},
        error => this.handleError(error));
  }

  identifier(): string {
    return 'change_status';
  }

  constructor(private http: HttpClient) {
    super();
  }
}
