package fr.unice.polytech.cli.commands.whatif;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class WhatIfIRemoveStory extends Command<Environment> implements WebCommand {

    private String sprintName;
    private List<String> storyNames;


    @Override
    public String identifier() { return "what_if_i_remove"; }

    @Override
    public Response execResponse() throws CmdException {
        return Response.ok(execWithResponse()).build();
    }

    @Override
    public void execute() {
        print(execWithResponse());
    }

    private String execWithResponse() {DTORepository repository = DTORepository.get();
        Sprint sprint = repository.getSprint(sprintName);

        repository.fill(sprint);

        if(sprint == null){
            return "Le sprint "+ sprintName+" n'existe pas\n";
        }

        List<UserStory> stories = repository.getStoriesIn(storyNames);
        if(stories.size() != storyNames.size()){
            List<String> temp = stories.stream().map(UserStory::getName).collect(Collectors.toList());
            String sts = storyNames.stream().filter(x -> !temp.contains(x)).collect(Collectors.joining(", "));
            return "Les stories "+sts+" n'existent pas\n";
        }

        List<UserStory> notContained = stories.stream().filter( x -> !sprint.getStoryList().contains(x)).collect(Collectors.toList());
        if(notContained.size() > 0){
            String sts = notContained.stream().map(UserStory::getName).collect(Collectors.joining(", "));
            return "Les stories " + sts + " n'appartiennent pas au Sprint " + sprintName;
        }

        int newBv = sprint.calculateTotalBusinessValue() - stories.stream().mapToInt(UserStory::getBusinessValue).sum() ;
        int newSp = sprint.calculateTotalStoryPoints() - stories.stream().mapToInt(UserStory::getStoryPoints).sum() ;
        return "[Actuellement     ] --> Business Value : " + sprint.calculateTotalBusinessValue() + " - Story Points : " + sprint.calculateTotalStoryPoints() +
        "\n[Apres Suppression]  --> Business Value : " + newBv + " - Story Points : " + newSp + "\n";
    }


    @Override
    public void load(List<String> args) {

        if(args.size() > 1){
            sprintName = args.get(0).trim();
            storyNames = args.subList(1,args.size()).stream().map(String::trim).collect(Collectors.toList());
        }else {
            System.out.println("Nombre d'arguments incorrect incorrect");
        }
    }


    @Override
    public String describe() {
        return "Display new Sprint's BV and SP if stories are removed : what_if_i_remove 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }

}

