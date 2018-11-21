package fr.unice.polytech.cli.commands;

import fr.unice.polytech.repository.dto.SprintStatDTO;
import fr.unice.polytech.repository.dto.StoryDTO;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CreateSprint extends AbstractSprintCommand {


    private List<StoryDTO> backlog;
    private boolean error;

    @Override
    public String identifier() { return "create_sprint"; }

    @Override
    public void load(List<String> args) {
        super.load(args);
        backlog = this.shell.system.getRepository().getBacklog();
        List<Integer> missingStories = this.storyIds.stream().filter(x -> !sprintNumberExists(x)).collect(Collectors.toList());
        if(missingStories.size() > 0){
            String sts = missingStories.stream().map(String::valueOf).collect(Collectors.joining(", "));
            this.error = true;
            print("Couldn't find stories n° " + sts);
        }
    }

    @Override
    protected int getNbOtherArgs() {
        return 0;
    }

    private boolean sprintNumberExists(Integer integer) {
        return this.backlog.stream().anyMatch( x -> x.getNumber() == integer);
    }

    @Override
    protected void check() throws IOException {
        if(this.shell.system.getRepository().getSprint(this.sprintName) != null) {
            throw new IOException("The sprint named " + this.sprintName + " already exists.");
        }
    }

    @Override
    public void execute() throws IOException {
        super.execute();
        if(error){
            return;
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

        this.shell.system.getRepository().executeQuery(resBuilder.toString());

        checkIfSprintLoadHigherThanTheAverage();

    }

    private void checkIfSprintLoadHigherThanTheAverage() {
        List<SprintStatDTO> list = this.shell.system.getRepository().getAllSprintStat();
        Double averagePrevPrintsStoryPoints = list.stream()
                .mapToInt(SprintStatDTO::getStoryPoints).average().getAsDouble();

        Double averageNewSprintStoryPoints = this.backlog
                .stream().filter(x -> storyIds.contains(x.getNumber()))
                .mapToInt(StoryDTO::getStoryPoints).average().getAsDouble();

        if(averageNewSprintStoryPoints > averagePrevPrintsStoryPoints){
            System.out.println("[WARNING] this sprint has more story points ("+averageNewSprintStoryPoints+") than the average ("+averagePrevPrintsStoryPoints+")");
        }
    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "       - name:String\n" +
                "       - stories:List<Integer>";
    }
}