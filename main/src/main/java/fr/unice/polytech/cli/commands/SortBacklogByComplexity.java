package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class SortBacklogByComplexity extends Command<Environment> {

    @Override
    public String identifier() {
        return "sort_backlog_by_complexity";
    }

    private String sprintName;

    @Override
    public void load(List<String> args) {
        sprintName = args.get(0);
    }

    @Override
    public void execute() {

        Sprint sp = DTORepository.get().getSprintWithUserStories(sprintName);
        List<UserStory> backlog = DTORepository.get().getBacklogUserStories();

        Map<UserStory,Integer> methodAdded = new HashMap<>();
        Map<UserStory,Integer> classesAdded = new HashMap<>();
        backlog.forEach(x -> {methodAdded.put(x,0);classesAdded.put(x,0);});

        backlog.forEach(us -> {
            us.getMethods().forEach( method -> {
                if(!sp.containsDomainElement(method).isPresent()){
                    methodAdded.put(us,methodAdded.get(us)+1);
                }
            });
        });

        backlog.forEach(us -> {
            us.getClasses().forEach( cl -> {
                if(!sp.containsDomainElement(cl).isPresent()){
                    classesAdded.put(us,classesAdded.get(us)+1);
                }
            });
        });


        Stream<Map.Entry<UserStory, Integer>> sorted = classesAdded.entrySet().stream().sorted(Map.Entry.comparingByValue());
        sorted.forEach(us -> System.out.println("Classes added by: " + us.getKey().getName() + " = " + us.getValue()));
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
