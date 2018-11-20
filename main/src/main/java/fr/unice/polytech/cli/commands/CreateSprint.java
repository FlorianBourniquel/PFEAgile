package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.repository.dto.StoryDTO;
import fr.unice.polytech.environment.Environment;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateSprint extends Command<Environment> {

    private String sprintName;

    private List<Integer> storyIds;

    public CreateSprint(){
        this.storyIds = new ArrayList<>();
        this.sprintName = "";
    }

    @Override
    public String identifier() { return "create_sprint"; }

    @Override
    public void load(List<String> args) {
        initCmd();

        if(args.size() > 0){
            sprintName = args.get(0);
            args.remove(0);

            args.forEach(s ->
                    storyIds.add(Integer.valueOf(s))
            );

            for (Integer integer :
                    this.storyIds) {
                if(!isIdExists(integer)){
                    throw new IllegalArgumentException("Couldn't find story n°" + integer);
                }
            }
        }
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

    private void initCmd() {
        this.storyIds.clear();
        this.sprintName = "";
    }

    @Override
    public void execute() throws IOException {
        if(this.sprintName.isEmpty()){
            throw new IOException("Please specify a name for the sprint.");
        } else if(this.checkSprintExistancy()){
            throw new IOException("The sprint named " + this.sprintName + " already exists.");
        }

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

    private boolean checkSprintExistancy() {
        try (Session session = shell.system.getDb().getDriver().session()) {
            StringBuilder resBuilder = new StringBuilder();

            resBuilder.append("MATCH (s:Sprint {name:\"").append(this.sprintName).append("\"}) return s");

            StatementResult result = session.writeTransaction(tx -> tx.run(resBuilder.toString()));

            return result.hasNext();
        }
    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "       - name:String\n" +
                "       - stories:List<Integer>";
    }
}