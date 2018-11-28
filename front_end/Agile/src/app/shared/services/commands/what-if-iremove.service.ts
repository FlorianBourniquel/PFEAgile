import { Injectable } from '@angular/core';
import {Command} from "./Command";
import {CmdRequestModel} from "../../models/CmdRequestModel";
import {WhatIfStats} from "../../models/WhatIfStats";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class WhatIfIRemoveService extends Command{

  identifier(): string {
    return "what_if_i_remove";
  }

  description(): string {
    return 'simule sur un sprint souhaité le retrait de certaines user stories et retourne l\'impact non métier';
  }

  exec(cmd: CmdRequestModel) {
    this.http.post<WhatIfStats>(this.base_url, cmd, { observe: 'response', responseType: 'json'})
      .subscribe(resp => this.printResponse(resp.body), error => this.handleError(error));
  }

  printResponse(result: WhatIfStats) {
    const output = '[Actuellement] --> Business Value : ' + result.beforeBusiness + ' - Story Points : ' + result.beforePoints
      + '<br>' + '[Apres Suppression]  --> Business Value : ' + result.afterBusiness + ' - Story Points : ' + result.afterPoints;

    this.displayOutput(output);
  }

  constructor(private http:HttpClient) {
    super();
  }
}
