package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.ClassStatus;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.List;


public class ChangeClassStatus extends Command<Environment> implements WebCommand {

    private String className;

    private ClassStatus classStatus;

    @Override
    public void load(List<String> args){
        className = args.get(0);

        if(args.size() > 1){
            this.classStatus = ClassStatus.valueOf(args.get(1));
        } else {
            this.classStatus = ClassStatus.OK;
        }
    }

    @Override
    public String identifier() {
        return "change_status";
    }

    @Override
    public Response execResponse() throws CmdException {
        return Response.ok(execWithResult()).build();
    }

    @Override
    public void execute() {
        print(execWithResult());
    }

    public String execWithResult(){
        DTORepository.get().changeStatus(className, classStatus);

        return "Class named " + className + " now marked with " + classStatus.name() + " status";
    }

    @Override
    public String describe() {
        return "Change the status of the specified class";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
