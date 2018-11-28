import {BehaviorSubject} from 'rxjs';
import {URLBACKEND} from '../../constants/urls';
import {CmdRequestModel} from '../../models/CmdRequestModel';
import {HttpErrorResponse} from '@angular/common/http';

export abstract class Command {

   protected output$: BehaviorSubject<string>;
   protected readonly base_url: string;
   abstract identifier(): string;
   abstract description(): string;
   abstract exec(args: CmdRequestModel);

   protected constructor() {
     this.base_url = URLBACKEND + '/main';
   }

   public init(output: BehaviorSubject<string>) {
     this.output$ = output;
   }

  public displayOutput(out: string) {
    this.output$.next(this.identifier() + ' > <br>' + out);
  }

  public handleError(error: HttpErrorResponse) {
    this.displayOutput( 'Erreur serveur lors de l\'execution de la commande');
  }


}
