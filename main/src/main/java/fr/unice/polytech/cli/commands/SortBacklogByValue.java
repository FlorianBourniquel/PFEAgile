package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.dto.StoryDTO;
import java.util.Comparator;
import java.util.List;


public class SortBacklogByValue extends Command<Environment> {

    @Override
    public String identifier() {
        return "sort_backlog_by_value";
    }

    @Override
    public void execute() {
        List<UserStory> stories = shell.system.getRepository().getBacklog();
        stories.sort(Comparator.comparing(UserStory::getAgileRatio).reversed());
        System.out.println();
        stories.forEach(x -> System.out.println(x.toStringWithRatio()));
        System.out.println();
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
