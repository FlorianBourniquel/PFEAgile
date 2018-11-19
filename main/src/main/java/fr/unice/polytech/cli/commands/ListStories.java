package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.dto.StoryDTO;
import fr.unice.polytech.environment.Environment;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.List;


public class ListStories extends Command<Environment> {

    @Override
    public String identifier() {
        return "list_stories";
    }

    @Override
    public void execute() {
        try (Session session = shell.system.getDb().getDriver().session()) {

            StatementResult s = session.writeTransaction(
                    tx -> tx.run(
                            "match (s:Story) return s"));

            List<StoryDTO> stories = s.list(r -> new StoryDTO(r.get("s").get("text").asString(), r.get("s").get("number").asInt()));

            for (StoryDTO story : stories) {

                System.out.println(story);
            }

            shell.system.setStories(stories);
        }

    }

    @Override
    public String describe() {
        return "init backlog";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

}
