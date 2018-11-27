package fr.unice.polytech.cli.commands;

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

public class RemoveStory extends Command<Environment> implements WebCommand{

    private String sprintName;
    private List<String> storyNames;

    @Override
    public String identifier() { return "remove_story"; }

    @Override
    public Response execResponse() throws CmdException {
        return Response.ok(executeWithResponse()).build();
    }

    @Override
    public void execute() {
        print(executeWithResponse());
    }

    private String executeWithResponse(){
        DTORepository repository = DTORepository.get();

        Sprint sprint = repository.getSprintWithUserStories(sprintName);
        if(sprint == null){
            return "Le sprint " + sprintName + " n'existe pas";
        }

        List<UserStory> stories = repository.getStoriesIn(storyNames);
        if(stories.size() != storyNames.size()){
            List<String> temp = stories.stream().map(UserStory::getName).collect(Collectors.toList());
            String sts = storyNames.stream().filter(x -> !temp.contains(x)).collect(Collectors.joining(", "));
            return "Les stories " + sts + " n'existent pas";
        }


        List<UserStory> notContained = stories.stream().filter( x -> ! sprint.getStoryList().contains(x)).collect(Collectors.toList());
        if(! notContained.isEmpty()){
            String sts = notContained.stream().map(UserStory::getName).collect(Collectors.joining(", "));
            return "Les stories " + sts + " n'appartiennent pas au sprint " + sprintName;
        }


        removeStories(sprint, stories);
        String sts = stories.stream().map(UserStory::getName).collect(Collectors.joining(", "));

        return "Les stories "+ sts + " ont été supprimées du sprint " + sprintName;
    }

    @Override
    public void load(List<String> args) {
        if(args.size() > 1){
            sprintName = args.get(0).trim();
            storyNames = args.subList(1,args.size()).stream().map(String::trim).collect(Collectors.toList());
        } else {
            print("Nombre d'arguments incorrect");
        }
    }


    @Override
    public String describe() {
        return "Remove a story from a sprint : remove_story 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }

    private void removeStories(Sprint sprint, List<UserStory> toDelete) {
        StringBuilder sb = new StringBuilder();
        UserStory story;
        for (int i = 0; i < toDelete.size(); i++) {
            story = toDelete.get(i);
            sb.append("MATCH (sp:Sprint{name:"+ss(sprint.getName())+"}) ")
              .append("-[c"+i+":CONTAINS]->")
              .append(" (:Story{name:"+ ss(story.getName()) +"})").append("\n");
        }

        for (int i = 0; i < toDelete.size(); i++) {
            sb.append("DELETE c"+i).append("\n");
        }

        if(sprint.getStoryList().size() == toDelete.size()){
            sb.append("DELETE sp");
        }

        String query = sb.toString();
        DTORepository.get().executeQuery(query);
    }


}

