import { Injectable } from '@angular/core';
import {Command} from "./Command";
import {CmdRequestModel} from "../../models/CmdRequestModel";
import {WhatIfStats} from "../../models/WhatIfStats";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class WhatIfIAddService extends Command{

  identifier(): string {
    return "what_if_i_add";
  }

  description(): string {
    return 'simule sur un sprint souhaité l\'ajout de certaines user stories et retourne l\'impact non métier';
  }

  exec(cmd: CmdRequestModel) {
    this.http.post<WhatIfStats>(this.base_url, cmd, { observe: 'response', responseType: 'json'})
      .subscribe(resp => this.printResponse(resp.body), error => this.handleError(error));
  }

  printResponse(result: WhatIfStats) {
    const output = '[Actuellement] --> Business Value : ' + result.beforeBusiness + ' - Story Points : ' + result.beforePoints
    + '<br>' + '[Apres Ajout]  --> Business Value : ' + result.afterBusiness + ' - Story Points : ' + result.afterPoints
    + '<br>';

    result.warnings.forEach(function (value) {
      output += ('<br>' + value);
    });

    this.displayOutput(output);
  }

  constructor(private http:HttpClient) {
    super();
  }
}
