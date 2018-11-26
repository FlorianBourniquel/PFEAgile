package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;

public class Class extends Node {

    private List<Method> methodList;

    public Class(String name) {
        super(name);
        this.methodList = new ArrayList<>();
        colorEnum = ColorEnum.CLASS;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }

    @Override
    public void fill(Session session) {
        StatementResult findRelationShip = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (r:RelationShip)<-[:CAN]-(n:Class {name:\"" + this.getName() + "\"}) RETURN r"));

        this.methodList = findRelationShip.list(methodElement -> new Method(methodElement.get("r").get("name").asString()));

    }
}
