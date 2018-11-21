package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.graphviz.*;
import fr.unice.polytech.graphviz.Class;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.util.*;

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

            Sprint sprint = new Sprint(new ArrayList<>(), findSprint.next().get("s").get("name").asString());

            sprint.setColorEnum(ColorEnum.MODIFIED);
            sprint.fill(session);

            for (UserStory story :
                    sprint.getStoryList()) {

                story.getClasses().forEach(c -> c.setColorEnum(ColorEnum.DEFAULT));
                story.getMethods().forEach(m -> m.setColorEnum(ColorEnum.DEFAULT));

            }

            for (Integer story :
                    this.storyIds) {
                StatementResult findStory = session.writeTransaction(
                        tx -> tx.run(
                                "MATCH (s:Story {name : \"US" + story + "\"}) return s"));

                List<UserStory> stories = findStory.list(s -> new UserStory(new ArrayList<>(), new ArrayList<>(), "US" + story));

                stories.forEach(s -> {
                    s.fill(session);

                    for (Class classElement:
                         s.getClasses()) {
                        Optional<Class> classOptional = sprint.containsDomainElement(classElement);

                        if(classOptional.isPresent()){
                            classOptional.get().setColorEnum(ColorEnum.MODIFIED);
                        } else {
                            classElement.setColorEnum(ColorEnum.ADDED);
                        }
                    }

                    for (Method methodElement:
                            s.getMethods()) {
                        Optional<Method> methodOptional = sprint.containsDomainElement(methodElement);

                        if(methodOptional.isPresent()){
                            methodOptional.get().setColorEnum(ColorEnum.MODIFIED);
                        } else {
                            methodElement.setColorEnum(ColorEnum.ADDED);
                        }
                    }

                    s.setColorEnum(ColorEnum.ADDED);
                    sprint.getStoryList().add(s);
                });
            }

            Parser.parse(Collections.singletonList(sprint), "/data/nodeAdd.csv","/data/edgeAdd.csv");
        }
    }

    @Override
    public void load(List<String> args) {
        super.load(args);

        if(args.size() > 0){
            addMode = Boolean.valueOf(args.get(0));
            args.remove(0);
        }
    }

    @Override
    protected void check() throws IOException {
        if(this.shell.system.getRepository().getSprint(this.sprintName) == null) {
            throw new IOException("The sprint named " + this.sprintName + " wasn't found.");
        }
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