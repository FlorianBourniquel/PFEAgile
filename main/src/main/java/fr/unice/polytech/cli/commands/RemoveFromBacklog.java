package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;


public class RemoveFromBacklog extends Command<Environment> implements WebCommand {

    private List<String> toRemove;

    @Override
    public String identifier() {
        return "remove_from_backlog";
    }

    @Override
    public Response execResponse() throws CmdException {
        System.out.println("called");
        return Response.ok("removed from the backlog").build();
    }

    @Override
    public void execute() {

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
