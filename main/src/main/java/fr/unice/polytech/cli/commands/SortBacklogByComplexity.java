package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class SortBacklogByComplexity extends Command<Environment> implements WebCommand{

    private String sprintName;

    @Override
    public String identifier() {
        return "sort_backlog_by_complexity";
    }

    @Override
    public Response execResponse() throws CmdException {
        Map<UserStory,Integer> resp = this.executeWithResult().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return Response.ok(resp).build();
    }

    @Override
    public void load(List<String> args) {
        sprintName = args.get(0);
    }

    @Override
    public void execute() {
        StringBuilder res = new StringBuilder();

        this.executeWithResult().forEach(us -> res.append("Classes added by: ").append(us.getKey().getName()).append(" = ").append(us.getValue()).append("\n"));

        print(res.toString());
    }

    private Stream<Map.Entry<UserStory, Integer>> executeWithResult(){
        Sprint sp = DTORepository.get().getSprintWithUserStories(sprintName);
        List<UserStory> backlog = DTORepository.get().getBacklogUserStories();

        sp.getStoryList().forEach(story -> DTORepository.get().fill(story));

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

        return classesAdded.entrySet().stream().sorted(Map.Entry.comparingByValue());
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
