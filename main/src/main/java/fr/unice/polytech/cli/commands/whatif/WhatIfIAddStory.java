package fr.unice.polytech.cli.commands.whatif;

import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.repository.dto.SprintStatDTO;
import fr.unice.polytech.repository.dto.SprintWithStoriesDTO;
import fr.unice.polytech.repository.dto.StoryDTO;
import fr.unice.polytech.environment.Environment;
import java.util.List;
import java.util.stream.Collectors;

public class WhatIfIAddStory extends Command<Environment> {

    private String sprintName;
    private List<String> storyNames;


    @Override
    public String identifier() { return "what_if_i_add"; }

    @Override
    public void execute() {
        DTORepository repository = this.shell.system.getRepository();
        SprintWithStoriesDTO sprint = repository.getSprintWithStories(sprintName);
        if(sprint == null){
            print("Le spring "+ sprintName+" n'existe pas");
            return;
        }

        List<StoryDTO> stories = repository.getStoriesIn(storyNames);
        if(stories.size() != storyNames.size()){
             List<String> temp = stories.stream().map(StoryDTO::getName).collect(Collectors.toList());
            String sts = storyNames.stream().filter(x -> !temp.contains(x)).collect(Collectors.joining(", "));
            System.out.println("Les stories "+sts+" n'existent pas");
            System.out.println();
            return;
        }

        List<StoryDTO> contained = stories.stream().filter( x -> sprint.getStories().contains(x)).collect(Collectors.toList());
        if(contained.size() > 0){
            String sts = contained.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
            print("Les stories " + sts + " appartiennent déja au Sprint " + sprintName);
            return;
        }


        SprintStatDTO sprintStat = repository.getSprintStat(sprint.getSprint().getName());
        int newBv = stories.stream().mapToInt(StoryDTO::getBusinessValue).sum() + sprintStat.getBusinessValue();
        int newSp = stories.stream().mapToInt(StoryDTO::getStoryPoints).sum() + sprintStat.getStoryPoints();
        print("[Actuellement] --> Business Value : " + sprintStat.getBusinessValue() + " - Story Points : " + sprintStat.getStoryPoints()
              + "\n[Apres Ajout]  --> Business Value : " + newBv + " - Story Points : " + newSp);
    }


    @Override
    public void load(List<String> args) {

        if(args.size() > 1){
            sprintName = args.get(0).trim();
            storyNames = args.subList(1,args.size()).stream().map(String::trim).collect(Collectors.toList());
        }else {
            print("Nombre d'arguments incorrect incorrect");
        }
    }


    @Override
    public String describe() {
        return "Display new Sprint's BV and SP if stories are added : what_if_i_add 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }

}

