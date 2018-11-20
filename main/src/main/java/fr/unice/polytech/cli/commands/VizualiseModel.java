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
    public String identifier() {
        return "visualise_domain";
    }

    @Override
    public void execute() throws IOException {
        List<Sprint> sprintList = new ArrayList<>();

        try (Session session = shell.system.getDb().getDriver().session()) {

            StatementResult findSprints = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Sprint) return s"));

            sprintList = findSprints.list(sprint -> new Sprint(new ArrayList<>(), sprint.get("s").get("name").asString()));

            for (Sprint sprint : sprintList) {
                StatementResult findStories = session.writeTransaction(
                        tx -> tx.run(
                                "MATCH (s)<-[:CONTAINS]-(n:Sprint {name:\"" + sprint.getName() + "\"}) RETURN s"));

                List<UserStory> stories = findStories.list(story -> new UserStory(new ArrayList<>(), new ArrayList<>(), story.get("s").get("name").asString()));

                sprint.setStoryList(stories);

                for (UserStory story : stories) {
                    StatementResult findClasses = session.writeTransaction(
                            tx -> tx.run(
                                    "MATCH (c:Class)<-[:INVOLVES]-(n:Story {name:\"" + story.getName() + "\"}) RETURN c"));

                    List<Class> classes = findClasses.list(classElement -> new Class(new ArrayList<>(), classElement.get("c").get("name").asString()));

                    story.setClasses(classes);

                    StatementResult findMethods = session.writeTransaction(
                            tx -> tx.run(
                                    "MATCH (c:RelationShip)<-[:INVOLVES]-(n:Story {name:\"" + story.getName() + "\"}) RETURN c"));

                    List<Method> methods = findMethods.list(methodElement -> new Method(new ArrayList<>(), methodElement.get("c").get("name").asString()));

                    story.setMethods(methods);

                    for (Class classElement : classes) {
                        StatementResult findRelationShip = session.writeTransaction(
                                tx -> tx.run(
                                        "MATCH (r:RelationShip)<-[:CAN]-(n:Class {name:\"" + classElement.getName() + "\"}) RETURN r"));

                        List<Method> methodsRelation = findRelationShip.list(methodElement -> new Method(new ArrayList<>(), methodElement.get("r").get("name").asString()));

                        classElement.setMethodList(methodsRelation);
                    }

                    for (Method methodElement : methods) {
                        StatementResult findRelationShip = session.writeTransaction(
                                tx -> tx.run(
                                        "MATCH (r:Class)<-[:TARGET]-(n:RelationShip {name:\"" + methodElement.getName() + "\"}) RETURN r"));

                        List<Class> classesRelation = findRelationShip.list(classElement -> new Class(new ArrayList<>(), classElement.get("r").get("name").asString()));

                        methodElement.setClassList(classesRelation);
                    }
                }
            }
        }

        //place here the call to the viz function
        this.parse(sprintList);
    }

    @Override
    public String describe() {
        return "Create a vizualisation of the current model domain";
    }

    public void parse(List<Sprint> sprints) throws IOException {
        List<String> linesCompleteGraph = new LinkedList<>();
        List<String> linesSimpleGraph = new LinkedList<>();
        List<String> linesMethodClass = new LinkedList<>();
        List<String> sprintUserStoryNodes = new LinkedList<>();
        List<String> classMethodNodes = new LinkedList<>();
        int clustercpt = 0;


        List<String> subGraphSprint = new LinkedList<>();
        subGraphSprint.add("subgraph cluster_0 {");
        subGraphSprint.add("style=filled;");
        subGraphSprint.add("color=lightgrey;");
        subGraphSprint.add("node [style=filled,color=orange2];");
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
        subGraphMethod.add("label = \"Method\";");

        List<List<String>> subGraph = new LinkedList<>();
        subGraph.add(subGraphSprint);
        subGraph.add(subGraphUserStory);
        subGraph.add(subGraphClass);
        subGraph.add(subGraphMethod);

        linesCompleteGraph.add("digraph G {");

        //Fill sprint subGraph
        linesCompleteGraph.addAll(subGraph.get(clustercpt++));
        for (Sprint sprint : sprints) {
            sprintUserStoryNodes.add(sprint.getName() +  "[style = filled,color=orange2];");
            linesCompleteGraph.add(sprint.getName() + ";");
        }
        linesCompleteGraph.add("}");

        //Fill story subGraph
        linesCompleteGraph.addAll(subGraph.get(clustercpt++));
        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                sprintUserStoryNodes.add(userStory.getName() + "[style = filled,color=chartreuse3];");
                linesCompleteGraph.add(userStory.getName() + ";");
            }
        }
        linesCompleteGraph.add("}");

        //Fill set Class and Method
        Set<Class> classes = new HashSet<>();
        Set<Method> methods = new HashSet<>();
        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                classes.addAll(userStory.getClasses());
                methods.addAll(userStory.getMethods());
            }
        }

        //Fill class subGraph
        linesCompleteGraph.addAll(subGraph.get(clustercpt++));
        for (Class aClass : classes) {
            classMethodNodes.add(aClass.getName() + "[style = filled,color=crimson];");
            linesCompleteGraph.add(aClass.getName() + ";");
        }
        linesCompleteGraph.add("}");

        //Fill method subGraph
        linesCompleteGraph.addAll(subGraph.get(clustercpt++));
        for (Method method : methods) {
            classMethodNodes.add(method.getName() + "[style = filled,color=darkorchid1];");
            linesCompleteGraph.add(method.getName() + ";");
        }
        linesCompleteGraph.add("}");

        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                linesSimpleGraph.add(sprint.getName() + "->" + userStory.getName() + "[color=pink4];");
                for (Class aClass : userStory.getClasses()) {
                    linesSimpleGraph.add(userStory.getName() + "->" + aClass.getName() + "[color=cadetblue];");
                }
                for (Method method : userStory.getMethods()) {
                    linesSimpleGraph.add(userStory.getName() + "->" + method.getName() + "[color=deeppink3];");
                }
            }
        }
        for (Class aClass : classes) {
            for (Method method : aClass.getMethodList()) {
                linesMethodClass.add(aClass.getName() + "->" + method.getName() + "[label=\"can\",color=midnightblue];");
            }
        }
        for (Method method : methods) {
            for (Class aClass : method.getClassList()) {
                linesMethodClass.add(method.getName() + "->" + aClass.getName() + "[label=\"target\",color=olivedrab3];");
            }
        }
        linesMethodClass.add("overlap=false;");
        linesMethodClass.add("splines = true;");
        linesMethodClass.add("}");

        linesSimpleGraph.addAll(linesMethodClass);
        linesCompleteGraph.addAll(linesSimpleGraph);

        linesMethodClass.add(0,"digraph G {");
        linesMethodClass.addAll(1,classMethodNodes);
        linesSimpleGraph.add(0,"digraph G {");
        linesSimpleGraph.addAll(1,sprintUserStoryNodes);
        linesSimpleGraph.addAll(2,classMethodNodes);

        Path fileComplete = Paths.get("/data/graphComplete.dot");
        Files.write(fileComplete, linesCompleteGraph, Charset.forName("UTF-8"));
        String svg = executeCommand("fdp -Tsvg /data/graphComplete.dot");
        Path fileSvg = Paths.get("/data/graphComplete.svg");
        Files.write(fileSvg, Collections.singleton(svg), Charset.forName("UTF-8"));

        Path fileNoCluster = Paths.get("/data/graphNoCluster.dot");
        Files.write(fileNoCluster, linesSimpleGraph, Charset.forName("UTF-8"));
        String svgNoCluster = executeCommand("fdp -Tsvg /data/graphNoCluster.dot");
        Path fileSvgNoCluster = Paths.get("/data/graphNoCluster.svg");
        Files.write(fileSvgNoCluster, Collections.singleton(svgNoCluster), Charset.forName("UTF-8"));

        Path fileMethodClass = Paths.get("/data/graphMethodClass.dot");
        Files.write(fileMethodClass, linesMethodClass, Charset.forName("UTF-8"));
        String svgMethodClass = executeCommand("fdp -Tsvg /data/graphMethodClass.dot");
        Path fileSvgMethodClass = Paths.get("/data/graphMethodClass.svg");
        Files.write(fileSvgMethodClass, Collections.singleton(svgMethodClass), Charset.forName("UTF-8"));
    }
}
