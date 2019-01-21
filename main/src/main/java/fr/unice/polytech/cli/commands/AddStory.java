package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.cli.warnings.CriticalClassWarning;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

public class AddStory extends Command<Environment> implements WebCommand{

    private String sprintName;
    private List<String> storyNames;

    @Override
    public String identifier() { return "add_story"; }

    @Override
    public Response execResponse() throws CmdException {
        String res = executeWithResponse();
        res += "\n";
        res += new CriticalClassWarning().check(storyNames);

        return Response.ok(res).build();
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

        List<UserStory> backlog = repository.getBacklog();
        List<UserStory> toAdd = stories.stream().filter( x -> !sprint.getStoryList().contains(x)).collect(Collectors.toList());
        List<UserStory> alreadyBounded = toAdd.stream().filter(x ->!backlog.contains(x)).collect(Collectors.toList());
        if (! alreadyBounded.isEmpty()){
            String sts = alreadyBounded.stream().map(UserStory::getName).collect(Collectors.joining(", "));
            return "Les stories " + sts + " sont déja sélectionnées dans d'autres sprints";
        }

        List<UserStory> contained = stories.stream().filter( x -> sprint.getStoryList().contains(x)).collect(Collectors.toList());
        if(! contained.isEmpty()){
            String sts = contained.stream().map(UserStory::getName).collect(Collectors.joining(", "));
            return "Les stories " + sts + " appartiennent déja au sprint " + sprintName + "\n";
        }

        addStories(sprint, toAdd);
        String sts = toAdd.stream().map(UserStory::getName).collect(Collectors.joining(", "));
        return "Les stories "+ sts + " ont été ajoutés au sprint " + sprintName;
    }

    @Override
    public void load(List<String> args) {

        if(args.size() > 1){
            sprintName = args.get(0).trim();
            storyNames = args.subList(1,args.size()).stream().map(String::trim).collect(Collectors.toList());
        }else {
            print("Nombre d'arguments incorrect");
        }
    }


    @Override
    public String describe() {
        return "Add a story into a sprint : add_story 'SPRINT' 'STORY1' 'STORY2' ...";
    }

    @Override
    public boolean shouldContinue() { return true; }


    private void addStories(Sprint sprint, List<UserStory> toAdd) {
        StringBuilder sb = new StringBuilder();
        UserStory story;
        for (int i = 0; i < toAdd.size(); i++) {
            story = toAdd.get(i);
            sb.append("MATCH (st"+i+":Story{name:"+ ss(story.getName()) +"})").append("\n");
        }

        sb.append("MATCH (sp:Sprint{name:"+ss(sprint.getName())+"})").append("\n");;

        for (int i = 0; i < toAdd.size(); i++) {
            sb.append("CREATE (sp) -[:CONTAINS]-> (st"+i+")");
        }

        String query = sb.toString();
        DTORepository.get().executeQuery(query);
    }


}

