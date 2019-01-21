
export class CmdRequestModel {

  public cmd: string;
  public args: string[];

  constructor( cmd: string, args: string[]){
    this.cmd = cmd;
    this.args = args;
  }
}
