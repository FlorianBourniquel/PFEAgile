package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.repository.dto.StoryDTO;
import fr.unice.polytech.environment.Environment;

import java.util.Comparator;
import java.util.List;


public class ListBacklog extends Command<Environment> {

    @Override
    public String identifier() {
        return "list_backlog";
    }

    @Override
    public void execute() {
        List<StoryDTO> stories = shell.system.getRepository().getBacklog();
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
