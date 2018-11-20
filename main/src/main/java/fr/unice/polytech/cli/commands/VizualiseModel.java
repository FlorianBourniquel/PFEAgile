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

public class VizualiseModel extends Command<Environment> {

    @Override
    public String identifier() { return "vizualise_domain"; }

    @Override
    public void execute() throws IOException {
        List<Sprint> sprintList = new ArrayList<>();

        try (Session session = shell.system.getDb().getDriver().session()) {

            StatementResult findSprints = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Sprint) return s"));

            sprintList = findSprints.list(sprint -> new Sprint(new ArrayList<>(), sprint.get("s").get("name").asString()));

            for (Sprint sprint :
                    sprintList) {
                StatementResult findStories = session.writeTransaction(
                        tx -> tx.run(
                                "MATCH (s)<-[:CONTAINS]-(n:Sprint {name:\"" + sprint.getName() + "\"}) RETURN s"));

                List<UserStory> stories = findStories.list(story -> new UserStory(new ArrayList<>(), new ArrayList<>(), story.get("s").get("name").asString()));

                sprint.setStoryList(stories);

                System.out.println(sprint.getStoryList().size());
            }
            System.out.println(sprintList);
        }

        //place here the call to the viz function
        this.parse(sprintList);
    }

    @Override
    public String describe() {
        return "Create a vizualisation of the current model domain";
    }

    public void parse(List<Sprint> sprints) throws IOException {
        List<String> lines = new LinkedList<>();
        int clustercpt = 0;


        List<String> subGraphSprint = new LinkedList<>();
        subGraphSprint.add("subgraph cluster_0 {");
        subGraphSprint.add("style=filled;");
        subGraphSprint.add("color=lightgrey;");
        subGraphSprint.add("node [style=filled,color=white];");
        subGraphSprint.add("label = \"Sprint\";");

        List<String> subGraphUserStory = new LinkedList<>();
        subGraphUserStory.add("subgraph cluster_1 {");
        subGraphUserStory.add("style=filled;");
        subGraphUserStory.add("color=lightgrey;");
        subGraphUserStory.add("node [style=filled,color=chartreuse3];");
        subGraphUserStory.add("label = \"User Story\";");

        List<String> subGraphClass = new LinkedList<>();
        subGraphClass.add("subgraph cluster_2 {");
        subGraphClass.add("style=filled;");
        subGraphClass.add("color=lightgrey;");
        subGraphClass.add("node [style=filled,color=crimson];");
        subGraphClass.add("label = \"Class\";");

        List<String> subGraphMethod = new LinkedList<>();
        subGraphMethod.add("subgraph cluster_3 {");
        subGraphMethod.add("style=filled;");
        subGraphMethod.add("color=lightgrey;");
        subGraphMethod.add("node [style=filled,color=darkorchid1];");
        subGraphMethod.add("label = \"Class\";");

        List<List<String>> subGraph = new LinkedList<>();
        subGraph.add(subGraphSprint);
        subGraph.add(subGraphUserStory);
        subGraph.add(subGraphClass);
        subGraph.add(subGraphMethod);

        //Fill sprint subGraph
        lines.add("digraph G {");
        lines.addAll(subGraph.get(clustercpt++));
        for (Sprint sprint: sprints) {
            lines.add(sprint.getName()+";");
        }
        lines.add("}");

        //Fill story subGraph
        lines.add("digraph G {");
        lines.addAll(subGraph.get(clustercpt++));
        for (Sprint sprint: sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                lines.add(userStory.getName() + ";");
            }
        }
        lines.add("}");

        //Fill set Class and Method
        Set<Class> classes = new HashSet<>();
        Set<Method> methods = new HashSet<>();
        for (Sprint sprint: sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                classes.addAll(userStory.getClasses());
                methods.addAll(userStory.getMethods());
            }
        }

        //Fill class subGraph
        lines.add("digraph G {");
        lines.addAll(subGraph.get(clustercpt++));
        for (Class aClass: classes) {
            lines.add(aClass.getName() + ";");
        }
        lines.add("}");

        //Fill method subGraph
        lines.add("digraph G {");
        lines.addAll(subGraph.get(clustercpt++));
        for (Method method: methods) {
            lines.add(method.getName() + ";");
        }
        lines.add("}");

        for (Sprint sprint: sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                lines.add(sprint.getName() + "->" + userStory.getName() + "[color=lightgrey];");
                for (Class aClass:classes) {
                    lines.add(userStory.getName() + "->" + aClass.getName() + "[color=cadetblue];");
                    for (Method method : aClass.getMethodList()) {
                        lines.add(aClass.getName() + "->" + method.getName() + "[label=\"can\",color=midnightblue];");
                    }
                }
                for (Method method :methods) {
                    lines.add(userStory.getName() + "->" + method.getName() + "[color=seagreen];");
                    for (Class aClass : method.getClassList()) {
                        lines.add(method.getName() + "->" + aClass.getName() + "[label=\"target\",color=olivedrab3];");
                    }
                }
            }
        }

        lines.add("}");
        Path file = Paths.get("graph.dot");
        Files.write(file, lines, Charset.forName("UTF-8"));
    }
}
