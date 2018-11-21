package fr.unice.polytech.cli.commands;

import fr.unice.polytech.repository.dto.StoryDTO;
import org.neo4j.driver.v1.Session;

import java.io.IOException;
import java.util.List;

public class CreateSprint extends AbstractSprintCommand {

    @Override
    public String identifier() { return "create_sprint"; }

    @Override
    public void load(List<String> args) {
        super.load(args);

        this.storyIds.forEach(s -> {
            if(!isIdExists(s)) {
                throw new IllegalArgumentException("Couldn't find story n°" + s);
            }
        });
    }

    @Override
    protected int getNbOtherArgs() {
        return 0;
    }

    private boolean isIdExists(Integer integer) {
        for (StoryDTO story :
                this.shell.system.getStories()) {
            if (story.getNumber() == integer) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() throws IOException {
        super.execute();

        System.out.println("User requested to create a sprint named " + this.sprintName + " and containing " + this.storyIds.size() + " stories.");

        StringBuilder resBuilder = new StringBuilder();

        if(this.storyIds.size() > 0) {
            resBuilder.append("MATCH ");
        }

        for (int i = 0; i < this.storyIds.size(); i++) {

            resBuilder.append("(s").append(i + 1).append(":Story {name:\"US").append(this.storyIds.get(i)).append("\"})");

            if(i != (this.storyIds.size() - 1)){
                resBuilder.append(",");
            }
        }

        resBuilder.append("\nMERGE (n:Sprint {name:'").append(this.sprintName).append("'})");

        for (int i = 0; i < this.storyIds.size(); i++) {
            resBuilder.append("\nCREATE (n)-[:CONTAINS]->(s").append(i + 1).append(")");
        }

        try (Session session = shell.system.getDb().getDriver().session()) {
            session.writeTransaction(tx -> tx.run(resBuilder.toString()));
        }
    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "       - name:String\n" +
                "       - stories:List<Integer>";
    }
}