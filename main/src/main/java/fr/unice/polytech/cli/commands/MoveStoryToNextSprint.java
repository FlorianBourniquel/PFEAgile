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

public class MoveStoryToNextSprint extends Command<Environment> implements WebCommand {

    private String sprintName;
    private String storyName;

    @Override
    public String identifier() {
        return "move_story_to_next_sprint";
    }

    @Override
    public Response execResponse() throws CmdException {
        return Response.ok(executeWithResponse()).build();
    }

    @Override
    public void execute() {
        print(executeWithResponse());
    }

    private String executeWithResponse() {
        DTORepository repository = DTORepository.get();

        Sprint sprint = repository.getSprintWithUserStories(sprintName);
        if (sprint == null) {
            return "Le sprint " + sprintName + " n'existe pas";
        }

        UserStory story = repository.getUSByName(storyName);
        if (story == null) {
            return "La us " + storyName + " n'existe pas";
        }

        if (sprint.getNextSprint() == null) {
            return "Aucun sprint suivant n'existe";
        }
        withdrawAndMoveStory(sprint, story);

        StringBuilder response = new StringBuilder();
        response.append("La story ").append(story.getName()).append(" ont été deplacée au sprint suivant").append(sprint.getNextSprint().getName());
        int nb = repository.CheckNumberOfWithDraw(story);
        if (nb >= 1) {
            response.append("\n Attention la userStory à déjà été déplacer ").append(nb).append("fois");
        }
        return response.toString();
    }

    @Override
    public void load(List<String> args) {
        if (args.size() > 1) {
            sprintName = args.get(0).trim();
            storyName = args.get(1).trim();
        } else {
            print("Nombre d'arguments incorrect");
        }
    }


    @Override
    public String describe() {
        return "Move a story to the next sprint: move_story_to_next_sprint sprintName userStoryName";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    private void withdrawAndMoveStory(Sprint sprint, UserStory userStory) {
        StringBuilder sb = new StringBuilder();

        sb.append("MATCH (sp:Sprint{name:").append(ss(sprint.getName())).append("}) ")
                .append("-[c:CONTAINS]->").append(" (us:Story{name:").append(ss(userStory.getName())).append("})");

        sb.append(",  (nsp:Sprint{name:").append(ss(sprint.getNextSprint().getName())).append("})");

        sb.append("\n");

        sb.append("DELETE c").append("\n");
        sb.append("CREATE (sp)-[:WITHDRAW]->(us)").append("\n");
        sb.append("CREATE (nsp)-[:CONTAINS]->(us)").append("\n");


        String query = sb.toString();
        DTORepository.get().executeQuery(query);
    }



}

