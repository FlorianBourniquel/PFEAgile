package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.Method;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class VisualiseModel extends Command<Environment> {

    @Override
    public String identifier() {
        return "visualise_domain";
    }

    @Override
    public void execute() throws IOException {
        List<Sprint> sprintList;

        try (Session session = shell.system.getDb().getDriver().session()) {

            StatementResult findSprints = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Sprint) return s"));

            sprintList = findSprints.list(sprint -> new Sprint(new ArrayList<>(), sprint.get("s").get("name").asString()));

            for (Sprint sprint : sprintList) {
                sprint.fill(session);

                for (UserStory story : sprint.getStoryList()) {

                    story.fill(session);

                    for (Class classElement : story.getClasses()) {
                        classElement.fill(session);
                    }

                    for (Method methodElement : story.getMethods()) {
                        methodElement.fill(session);
                    }
                }
            }
        }

        this.parse(sprintList);
    }

    @Override
    public String describe() {
        return "Create a vizualisation of the current model domain";
    }

    public void parse(List<Sprint> sprints) throws IOException {
        List<String> linesNode = new LinkedList<>();
        List<String> linesEdge = new LinkedList<>();

        linesNode.add("id,type,type.label,size,node.color");
        linesEdge.add("from,to,type,weight");

        for (Sprint sprint : sprints) {
            linesNode.add(sprint.getName() + "," + 1 + "," + "Sprint" + "," + 10 + "," + sprint.getColorEnum().getColor());
        }
        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                linesNode.add(userStory.getName() + "," + 2 + "," + "USERSTORY" + "," + 10 + "," + userStory.getColorEnum().getColor());
            }
        }

        //Fill set Class and Method
        Set<Class> classes = new HashSet<>();
        Set<Method> methods = new HashSet<>();
        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                classes.addAll(userStory.getClasses());
                methods.addAll(userStory.getMethods());
            }
        }

        for (Class aClass : classes) {
            linesNode.add(aClass.getName() + "," + 3 + "," + "CLASS" + "," + 10 + "," + aClass.getColorEnum().getColor());
        }
        for (Method method : methods) {
            linesNode.add(method.getName() + "," + 4 + "," + "METHOD" + "," + 10 + "," + method.getColorEnum().getColor());
        }

        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                linesEdge.add(sprint.getName() + "," + userStory.getName() + "," + "contains" + "," + 20);
                for (Class aClass : userStory.getClasses()) {
                    linesEdge.add(userStory.getName() + "," + aClass.getName() + "," + "involves" + "," + 20);
                }
                for (Method method : userStory.getMethods()) {
                    linesEdge.add(userStory.getName() + "," + method.getName() + "," + "involves" + "," + 20);
                }
            }
        }
        for (Class aClass : classes) {
            for (Method method : aClass.getMethodList()) {
                linesEdge.add(aClass.getName() + "," + method.getName() +  "," + "can" + "," + 20);
            }
        }
        for (Method method : methods) {
            for (Class aClass : method.getClassList()) {
                linesEdge.add(method.getName() + "," + aClass.getName() +  "," + "target" + "," + 20);
            }
        }

        Path fileNode = Paths.get("/data/node.csv");
        Files.write(fileNode, linesNode, Charset.forName("UTF-8"));

        Path fileEdge = Paths.get("/data/edge.csv");
        Files.write(fileEdge, linesEdge, Charset.forName("UTF-8"));
    }
}
