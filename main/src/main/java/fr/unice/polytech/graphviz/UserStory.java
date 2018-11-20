package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;

public class UserStory extends Node{

    private List<Class> classes;

    private List<Method> methods;

    public UserStory(List<Class> classes, List<Method> methods, String name) {
        super(name);
        this.classes = classes;
        this.methods = methods;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    @Override
    public void fill(Session session) {
        StatementResult findClasses = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (c:Class)<-[:INVOLVES]-(n:Story {name:\"" + this.getName() + "\"}) RETURN c"));

        this.classes = findClasses.list(classElement -> new Class(new ArrayList<>(), classElement.get("c").get("name").asString()));

        StatementResult findMethods = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (c:RelationShip)<-[:INVOLVES]-(n:Story {name:\"" + this.getName() + "\"}) RETURN c"));

        this.methods = findMethods.list(methodElement -> new Method(new ArrayList<>(), methodElement.get("c").get("name").asString()));

    }
}
