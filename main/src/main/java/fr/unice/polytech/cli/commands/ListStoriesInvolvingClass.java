package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;
import javax.ws.rs.core.Response;
import java.util.List;


public class ListStoriesInvolvingClass extends Command<Environment> implements WebCommand {


    private String className;
    private String sprintName;

    @Override
    public String identifier() {
        return "list_stories_involving_class";
    }


    @Override
    public void load(List<String> args) {
        if (args.size() < 1){
            throw new IllegalArgumentException("Veuillez entrer au moins le nom de la classe");
        }
        className = args.get(0);

        if (args.size() > 1){
            sprintName = args.get(1);
        }
    }

    @Override
    public Response execResponse() throws CmdException {
        try {
            List<UserStory> stories = compute();
            return Response.ok(stories).build();
        } catch (Exception e) {
            throw new CmdException(e.getMessage());
        }
    }

    @Override
    public void execute() {
        try {
            List<UserStory> stories = compute();
            System.out.println();
            stories.forEach(System.out::println);
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private List<UserStory> compute () throws Exception {
        DTORepository repo = DTORepository.get();
        Class cl = repo.getClassByName(className);
        if (cl == null){
            throw new Exception("La classe "+className+" n'existe pas");
        }
        if(sprintName == null){
            return  repo.getStoriesInvolvingClass(cl);
        }else {
            Sprint sprint = DTORepository.get().getSprint(sprintName);
            if (sprint == null){
                throw new Exception("Le sprint "+sprintName+" n'existe pas");
            }
            return  repo.getStoriesInvolvingClassInSprint(cl,sprint);
        }
    }

    @Override
    public String describe() {
        return "List stories involving class";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
