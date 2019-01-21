package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;


public class ListSprint extends Command<Environment> implements WebCommand {

    @Override
    public String identifier() {
        return "list_sprint";
    }

    @Override
    public Response execResponse() throws CmdException {
        List<Sprint> sprints = DTORepository.get().getAllSprints();
        sprints.sort(Comparator.comparing(Sprint::getName));
        return Response.ok(sprints).build();
    }

    @Override
    public void execute() {
        List<Sprint> sprints = DTORepository.get().getAllSprints();
        sprints.sort(Comparator.comparing(Sprint::getName).reversed());
        System.out.println();
        sprints.forEach(System.out::println);
        System.out.println();
    }

    @Override
    public String describe() {
        return "Display sprint";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
