package fr.unice.polytech.cli.commands;

import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.ClassStatus;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateSprint extends AbstractSprintCommand implements WebCommand {

    private List<UserStory> backlog;
    private boolean error;

    @Override
    public String identifier() { return "create_sprint"; }

    @Override
    public Response execResponse() throws CmdException {
        try {
            String res = this.checkIfSprintLoadHigherThanTheAverage();
            res += "\"";
            res += this.checkIfClassAreCriticals();


            execute();

            return Response.ok(res).build();
        } catch (IOException e) {
            throw new CmdException(e.getMessage());
        }
    }

    @Override
    public void load(List<String> args) {
        super.load(args);
        backlog = DTORepository.get().getBacklog();
        List<String> missingStories = this.storyIds.stream().filter(x -> !sprintNumberExists(x)).collect(Collectors.toList());
        if(missingStories.size() > 0){
            String sts = missingStories.stream().map(String::valueOf).collect(Collectors.joining(", "));
            this.error = true;
            print("Couldn't find stories " + sts);
        }
    }

    @Override
    protected int getNbOtherArgs() {
        return 0;
    }

    private boolean sprintNumberExists(String name) {
        return this.backlog.stream().anyMatch( x -> x.getName().equalsIgnoreCase(name));
    }

    @Override
    protected void check() throws IOException {
        if(DTORepository.get().getSprint(this.sprintName) != null) {
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

            resBuilder.append("(s").append(i + 1).append(":Story {name:\"").append(this.storyIds.get(i)).append("\"})");

            if(i != (this.storyIds.size() - 1)){
                resBuilder.append(",");
            }
        }

        System.out.println(checkIfSprintLoadHigherThanTheAverage());
        System.out.println(checkIfClassAreCriticals());

        resBuilder.append("\nMERGE (n:Sprint {name:'").append(this.sprintName).append("'})");

        for (int i = 0; i < this.storyIds.size(); i++) {
            resBuilder.append("\nCREATE (n)-[:CONTAINS]->(s").append(i + 1).append(")");
        }

        DTORepository.get().executeQuery(resBuilder.toString());

        Sprint lastSprint = DTORepository.get().getLastSprint();
        if (lastSprint != null) {
            resBuilder = new StringBuilder();
            resBuilder.append("MATCH (n:Sprint {name:'").append(this.sprintName).append("'}),");
            resBuilder.append(" (s:Sprint {name:'").append(lastSprint.getName()).append("'})");
            resBuilder.append("CREATE (s)-[:NEXT]->(n)");
            DTORepository.get().executeQuery(resBuilder.toString());
        }

    }

    private String checkIfSprintLoadHigherThanTheAverage() {
        List<Sprint> list = DTORepository.get().getAllSprints();

        if(list.size() > 0) {
            Double averagePrevPrintsStoryPoints = list.stream()
                    .mapToInt(Sprint::calculateTotalStoryPoints).average().getAsDouble();

            int averageNewSprintStoryPoints = this.backlog
                    .stream().filter(x -> storyIds.contains(x.getName()))
                    .mapToInt(UserStory::getStoryPoints).sum();

            if (averageNewSprintStoryPoints > averagePrevPrintsStoryPoints) {
                return "[WARNING] this sprint has more story points (" + averageNewSprintStoryPoints + ") than the average (" + averagePrevPrintsStoryPoints + ")";
            } else {
                return "This sprint contains the average story points from all the other sprints";
            }
        } else {
            return "This sprint is the first one, can't establish sprint load stats";
        }
    }

    private String checkIfClassAreCriticals() {
        List<Class> classes = DTORepository.get().getClassesIn(this.storyIds);

        List<Class> riskyClasses = new ArrayList<>();

        classes.forEach(aClass -> {
            if (aClass.getClassStatus() != ClassStatus.OK){
                riskyClasses.add(aClass);
            }
        });

        if(riskyClasses.isEmpty()){
            return "This sprint contains no risky classes";
        } else {
            List<String> temp = riskyClasses.stream().map(c -> c.getName() + "(marked as " + c.getClassStatus() + ")").collect(Collectors.toList());
            String sts = temp.stream().collect(Collectors.joining(", "));

            return "This sprint will modify the classes " + sts;
        }
    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "       - name:String\n" +
                "       - stories:List<Integer>";
    }
}