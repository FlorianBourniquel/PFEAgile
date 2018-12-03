package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.List;


public class RemoveFromBacklog extends Command<Environment> implements WebCommand {

    private List<String> toRemove;

    @Override
    public String identifier() {
        return "remove_from_backlog";
    }

    @Override
    public void load(List<String> args){
        toRemove = args;
    }

    @Override
    public Response execResponse() throws CmdException {
        this.execute();

        StringBuilder res = new StringBuilder();

        res.append("The US ");

        this.toRemove.forEach(us -> res.append(us).append(", "));

        res.append("have been removed from the backlog");

        return Response.ok(res.toString()).build();
    }

    @Override
    public void execute() {
        for (String remove :
                toRemove) {
            DTORepository.get().removeUSFromBacklog(remove);
        }
    }

    @Override
    public String describe() {
        return "Remove the requested user stories from the backlog";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
