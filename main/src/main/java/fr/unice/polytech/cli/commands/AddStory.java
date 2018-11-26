package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.repository.dto.SprintDTO;
import fr.unice.polytech.repository.dto.SprintWithStoriesDTO;
import fr.unice.polytech.repository.dto.StoryDTO;
import java.util.List;
import java.util.stream.Collectors;

public class AddStory extends Command<Environment> {

    private String sprintName;
    private List<String> storyNames;

    @Override
    public String identifier() { return "add_story"; }

    @Override
    public void execute() {
        DTORepository repository = this.shell.system.getRepository();

        SprintWithStoriesDTO sprint = repository.getSprintWithStories(sprintName);
        if(sprint == null){
            print("Le sprint "+ sprintName+" n'existe pas");
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


        List<StoryDTO> backlog = shell.system.getRepository().getBacklog();
        List<StoryDTO> toAdd = stories.stream().filter( x -> !sprint.getStories().contains(x)).collect(Collectors.toList());
        List<StoryDTO> alreadyBounded = toAdd.stream().filter(x ->!backlog.contains(x)).collect(Collectors.toList());
        if (! alreadyBounded.isEmpty()){
            String sts = alreadyBounded.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
            print("Les stories "+ sts + " sont déja sélectionnées dans d'autres sprints");
            return;
        }


        System.out.println();
        List<StoryDTO> contained = stories.stream().filter( x -> sprint.getStories().contains(x)).collect(Collectors.toList());
        if(! contained.isEmpty()){
            String sts = contained.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
            System.out.println("Les stories " + sts + " appartiennent déja au sprint " + sprintName + "\n");
        }

        if(! toAdd.isEmpty()){
            addStories(sprint,toAdd);
            String sts = toAdd.stream().map(StoryDTO::getName).collect(Collectors.joining(", "));
            System.out.println("Les stories "+ sts + " ont été ajoutés au sprint " + sprintName);
            System.out.println();
        }
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
        return "Add a story into a sprint : add_story 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }


    private void addStories(SprintWithStoriesDTO sprint, List<StoryDTO> toAdd) {
        StringBuilder sb = new StringBuilder();
        SprintDTO sp = sprint.getSprint();
        StoryDTO story;
        for (int i = 0; i < toAdd.size(); i++) {
            story = toAdd.get(i);
            sb.append("MATCH (st"+i+":Story{name:"+ ss(story.getName()) +"})").append("\n");
        }

        sb.append("MATCH (sp:Sprint{name:"+ss(sp.getName())+"})").append("\n");;

        for (int i = 0; i < toAdd.size(); i++) {
            sb.append("CREATE (sp) -[:CONTAINS]-> (st"+i+")");
        }

        String query = sb.toString();
        this.shell.system.getRepository().executeQuery(query);
    }


}

