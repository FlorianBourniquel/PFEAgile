package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.repository.dto.StoryDTO;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.web.WebCommand;
import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;


public class ListStories extends Command<Environment> implements WebCommand {

    @Override
    public String identifier() {
        return "list_stories";
    }

    @Override
    public Response execResponse() {
        List<StoryDTO> stories = DTORepository.get().getBacklog();
        return Response.ok(stories).build();
    }

    @Override
    public void execute() {
        List<StoryDTO> stories = DTORepository.get().getBacklog();
        stories.sort(Comparator.comparing(StoryDTO::getNumber));
        System.out.println();
        stories.forEach(System.out::println);
        System.out.println();
        shell.system.setStories(stories);
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
