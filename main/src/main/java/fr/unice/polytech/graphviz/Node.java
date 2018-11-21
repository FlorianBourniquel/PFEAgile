package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;

import java.util.Objects;

public abstract class Node {

    protected String name;

    protected ColorEnum colorEnum;

    public Node(String name){
        this.name = name;
        this.colorEnum = ColorEnum.DEFAULT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void fill(Session session);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public ColorEnum getColorEnum() {
        return colorEnum;
    }
}
