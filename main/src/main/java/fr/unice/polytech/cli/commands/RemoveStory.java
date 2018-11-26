package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.repository.dto.SprintDTO;
import fr.unice.polytech.repository.dto.SprintWithStoriesDTO;
import fr.unice.polytech.repository.dto.StoryDTO;

import java.util.List;
import java.util.stream.Collectors;

public class RemoveStory extends Command<Environment> {

    private String sprintName;
    private List<String> storyNames;


    @Override
    public String identifier() { return "remove_story"; }

    @Override
    public void execute() {
        DTORepository repository = this.shell.system.getRepository();

        SprintWithStoriesDTO sprint = repository.getSprintWithStories(sprintName);
        if(sprint == null){
            print("Le spring "+ sprintName+" n'existe pas");
            return;
        }
    /*
        List<StoryDTO> stories = repository.getStoriesIn(storyNames);
        if(stories.size() != storyNames.size()){
             List<String> temp = stories.stream().map(StoryDTO::getName).collect(Collectors.toList());
            String sts = storyNames.stream().filter(x -> !temp.contains(x)).collect(Collectors.joining(", "));
            print("Les stories "+sts+" n'existent pas");
            return;
        }

        List<StoryDTO> notContained = stories.stream().filter( x -> ! sprint.getStories().contains(x)).collect(Collectors.toList());
        if(! notContained.isEmpty()){
            String sts = notContained.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
            print("Les stories " + sts + " n'appartiennent pas au sprint " + sprintName);
            return;
        }

        removeStories(sprint,stories);
        String sts = stories.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
        System.out.println("Les stories "+ sts + " ont été supprimées du sprint " + sprintName);
        System.out.println();
        */
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
        return "Remove a story from a sprint : remove_story 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }


    private void removeStories(SprintWithStoriesDTO sprint, List<StoryDTO> toDelete) {
        StringBuilder sb = new StringBuilder();
        SprintDTO sp = sprint.getSprint();
        StoryDTO story;
        for (int i = 0; i < toDelete.size(); i++) {
            story = toDelete.get(i);
            sb.append("MATCH (sp:Sprint{name:"+ss(sp.getName())+"}) ")
              .append("-[c"+i+":CONTAINS]->")
              .append(" (:Story{name:"+ ss(story.getName()) +"})").append("\n");
        }

        for (int i = 0; i < toDelete.size(); i++) {
            sb.append("DELETE c"+i).append("\n");
        }

        if(sprint.getStories().size() == toDelete.size()){
            sb.append("DELETE sp");
        }

        String query = sb.toString();
        this.shell.system.getRepository().executeQuery(query);
    }


}

