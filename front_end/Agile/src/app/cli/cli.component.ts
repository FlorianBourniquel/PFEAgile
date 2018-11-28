import { Component, OnInit } from '@angular/core';
import {CmdProcessorService} from '../shared/services/cmd-processor/cmd-processor.service';

@Component({
  selector: 'app-cli',
  templateUrl: './cli.component.html',
  styleUrls: ['./cli.component.css']
})
export class CliComponent implements OnInit {

  public output = '';
  public input: string;

  constructor(private cmdProcessor: CmdProcessorService) {
  }

  ngOnInit() {
    this.cmdProcessor.cmdOutput$.subscribe( o => this.output = o);
  }

  public onSubmit(e: Event) {
    e.preventDefault();
    this.processInput();
  }

  keyPressed(e: KeyboardEvent) {
     if ( e.key === 'Enter') {
       this.processInput();
     }
  }

  private processInput() {
    if (this.input === undefined || this.input.trim().length === null) {
      this.output = 'Enter a command please';
      return;
    }
    const inputs = this.input.trim().split(new RegExp('\\s+'));
    const cmd = inputs[0];
    const  args = inputs.length > 1 ? inputs.slice(1) : [];
    this.cmdProcessor.execCmd(cmd, args);
  }
}
