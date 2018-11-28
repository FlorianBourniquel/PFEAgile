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


public class ListBacklog extends Command<Environment> implements WebCommand {

    @Override
    public String identifier() {
        return "list_backlog";
    }

    @Override
    public Response execResponse() throws CmdException {
        List<UserStory> stories = DTORepository.get().getBacklog();
        stories.sort(Comparator.comparing(UserStory::getNumber));

        return Response.ok(stories).build();
    }

    @Override
    public void execute() {
        List<UserStory> stories = DTORepository.get().getBacklog();
        stories.sort(Comparator.comparing(UserStory::getNumber));
        System.out.println();
        stories.forEach(System.out::println);
        System.out.println();
    }

    @Override
    public String describe() {
        return "Display remaining stories inside backlog";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
