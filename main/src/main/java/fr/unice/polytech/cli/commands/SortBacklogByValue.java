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


public class SortBacklogByValue extends Command<Environment> implements WebCommand{

    @Override
    public String identifier() {
        return "sort_backlog_by_value";
    }

    @Override
    public Response execResponse() throws CmdException {
        return Response.ok(sortRemainingUSByValue()).build();
    }

    @Override
    public void execute() {
        System.out.println();
        sortRemainingUSByValue().forEach(x -> System.out.println(x.toStringWithRatio()));
        System.out.println();
    }

    private List<UserStory> sortRemainingUSByValue(){
        List<UserStory> stories = DTORepository.get().getBacklog();
        stories.sort(Comparator.comparing(UserStory::getAgileRatio).reversed());

        return stories;
    }

    @Override
    public String describe() {
        return "Display remaining stories sorted by BV/SP";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
