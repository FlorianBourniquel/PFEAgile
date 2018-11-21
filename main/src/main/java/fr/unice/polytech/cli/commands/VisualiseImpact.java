package fr.unice.polytech.cli.commands;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.util.List;

public class VisualiseImpact extends AbstractSprintCommand {

    private boolean addMode;

    public VisualiseImpact() {
        this.addMode = true;
    }

    @Override
    public String identifier() {
        return "visualise_impact";
    }

    @Override
    public void execute() throws IOException {
        if(addMode){
            visualiseAddStories();
        } else {
            visualiseRemoveStories();
        }
    }

    private void visualiseRemoveStories() {

    }

    private void visualiseAddStories() throws IOException {
        super.execute();

        try (Session session = shell.system.getDb().getDriver().session()) {

            StatementResult findSprint = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Sprint {name : \"" + this.sprintName + "\"}) return s"));

            findSprint.next();
        }
    }

    @Override
    public void load(List<String> args) {
        super.load(args);

        if(args.size() > 0){
            addMode = Boolean.valueOf(args.get(0));
            args.remove(0);
        }

        throw new IllegalArgumentException("");
    }

    @Override
    protected int getNbOtherArgs() {
        return 1;
    }

    @Override
    public String describe() {
        return "Visualise the impact of a story on the selected sprint\n" +
                "       - name:String\n" +
                "       - addMode:Boolean\n" +
                "       - stories:List<Integer>";
    }
}