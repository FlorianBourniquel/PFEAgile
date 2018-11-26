package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.Method;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisualiseModel extends Command<Environment> {

    @Override
    public String identifier() {
        return "visualise_domain";
    }

    @Override
    public void execute() throws IOException {
        List<Sprint> sprintList;

        try (Session session = DTORepository.get().getDb().getDriver().session()) {

            StatementResult findSprints = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Sprint) return s"));

            sprintList = findSprints.list(sprint -> new Sprint(new ArrayList<>(), sprint.get("s").get("name").asString()));

            for (Sprint sprint : sprintList) {
                sprint.fill(session);
            }
        }

        Parser.parse(sprintList,"/data/node.csv","/data/edge.csv");
    }

    @Override
    public String describe() {
        return "Create a vizualisation of the current model domain";
    }

}
