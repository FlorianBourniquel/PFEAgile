package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;

public class Method extends Node {

    private List<Class> classList;

    public Method(List<Class> classList, String name) {
        super(name);
        this.classList = classList;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }

    @Override
    public void fill(Session session) {
        StatementResult findRelationShip = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (r:Class)<-[:TARGET]-(n:RelationShip {name:\"" + this.getName() + "\"}) RETURN r"));

        this.classList = findRelationShip.list(classElement -> new Class(new ArrayList<>(), classElement.get("r").get("name").asString()));

    }
}
