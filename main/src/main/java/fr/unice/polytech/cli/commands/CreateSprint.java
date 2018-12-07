package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.warnings.CriticalClassWarning;
import fr.unice.polytech.cli.warnings.SprintLoadWarning;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.io.IOException;
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
            String res = new SprintLoadWarning().check(this.storyIds, this.backlog);
            res += "\"";
            res += new CriticalClassWarning().check(this.storyIds);


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
        Sprint lastSprint = DTORepository.get().getLastSprint();
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

        System.out.println(new SprintLoadWarning().check(this.storyIds, this.backlog));
        System.out.println(new CriticalClassWarning().check(this.storyIds));

        resBuilder.append("\nMERGE (n:Sprint {name:'").append(this.sprintName).append("'})");

        for (int i = 0; i < this.storyIds.size(); i++) {
            resBuilder.append("\nCREATE (n)-[:CONTAINS]->(s").append(i + 1).append(")");
        }

        DTORepository.get().executeQuery(resBuilder.toString());


        if (lastSprint != null) {
            resBuilder = new StringBuilder();
            resBuilder.append("MATCH (n:Sprint {name:'").append(this.sprintName).append("'}),");
            resBuilder.append(" (s:Sprint {name:'").append(lastSprint.getName()).append("'})");
            resBuilder.append("CREATE (s)-[:NEXT]->(n)");
            DTORepository.get().executeQuery(resBuilder.toString());
        }

    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "       - name:String\n" +
                "       - stories:List<Integer>";
    }
}