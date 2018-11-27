package fr.unice.polytech.cli.commands.utils;

import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.Method;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class  Parser {

    public static void parseSprints(List<Sprint> sprints, String fileNodeName, String fileEdgeName) throws IOException {
        List<String> linesNode = new LinkedList<>();
        List<String> linesEdge = new LinkedList<>();

        linesNode.add("id,type,type.label,size,node.color,filter");
        linesEdge.add("from,to,type,weight");

        for (Sprint sprint : sprints) {
            linesNode.add(sprint.getName() + "," + 1 + "," + "Sprint" + "," + 10 + "," + sprint.getColorEnum().getColor() + "," + "Sprint&US");
        }
        for (Sprint sprint : sprints) {
            for (UserStory userStory : sprint.getStoryList()) {
                linesNode.add(userStory.getName() + "," + 2 + "," + "USERSTORY" + "," + 10 + "," + userStory.getColorEnum().getColor()+ "," + "Sprint&US");
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
            linesNode.add(aClass.getName() + "," + 3 + "," + "CLASS" + "," + 10 + "," + aClass.getColorEnum().getColor() + "," + "Class&Method");
        }
        for (Method method : methods) {
            linesNode.add(method.getName() + "," + 4 + "," + "METHOD" + "," + 10 + "," + method.getColorEnum().getColor() + "," + "Class&Method");
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

        Path fileNode = Paths.get(fileNodeName);
        Files.write(fileNode, linesNode, Charset.forName("UTF-8"));

        Path fileEdge = Paths.get(fileEdgeName);
        Files.write(fileEdge, linesEdge, Charset.forName("UTF-8"));
    }

    public static void parseUS(List<UserStory> userStories, String fileNodeName, String fileEdgeName) throws IOException {
        List<String> linesNode = new LinkedList<>();
        List<String> linesEdge = new LinkedList<>();

        linesNode.add("id,type,type.label,size,node.color,filter");
        linesEdge.add("from,to,type,weight");

        for (UserStory userStory : userStories) {
            linesNode.add(userStory.getName() + "," + 2 + "," + "USERSTORY" + "," + 10 + "," + userStory.getColorEnum().getColor()+ "," + "Sprint&US");
        }

        //Fill set Class and Method
        Set<Class> classes = new HashSet<>();
        Set<Method> methods = new HashSet<>();
        for (UserStory userStory : userStories) {
            classes.addAll(userStory.getClasses());
            methods.addAll(userStory.getMethods());
        }

        for (Class aClass : classes) {
            linesNode.add(aClass.getName() + "," + 3 + "," + "CLASS" + "," + 10 + "," + aClass.getColorEnum().getColor() + "," + "Class&Method");
        }
        for (Method method : methods) {
            linesNode.add(method.getName() + "," + 4 + "," + "METHOD" + "," + 10 + "," + method.getColorEnum().getColor() + "," + "Class&Method");
        }

        for (UserStory userStory : userStories) {
            linesEdge.add(userStory.getName() + "," + "contains" + "," + 20);
            for (Class aClass : userStory.getClasses()) {
                linesEdge.add(userStory.getName() + "," + aClass.getName() + "," + "involves" + "," + 20);
            }
            for (Method method : userStory.getMethods()) {
                linesEdge.add(userStory.getName() + "," + method.getName() + "," + "involves" + "," + 20);
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

        Path fileNode = Paths.get(fileNodeName);
        Files.write(fileNode, linesNode, Charset.forName("UTF-8"));

        Path fileEdge = Paths.get(fileEdgeName);
        Files.write(fileEdge, linesEdge, Charset.forName("UTF-8"));
    }
}
