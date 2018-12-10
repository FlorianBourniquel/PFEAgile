package fr.unice.polytech.models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Objects;

public class RelationShip {

    private String name;

    private Class source;

    private Class destination;

    public RelationShip(Node node, List<Class> classes) {
        this.name = ((Element)node).getAttribute("rdf:about").split("#")[1];
        String source =  ((Element)((Element) node).getElementsByTagName("rdfs:domain").item(0)).getAttribute("rdf:resource").split("#")[1];
        this.source = classes.stream().filter(x->x.getName().equals(source)).findFirst().get();
        String destination =  ((Element)((Element) node).getElementsByTagName("rdfs:range").item(0)).getAttribute("rdf:resource").split("#")[1];
        this.destination = classes.stream().filter(x->x.getName().equals(destination)).findFirst().get();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getSource() {
        return source;
    }

    public void setSource(Class source) {
        this.source = source;
    }

    public Class getDestination() {
        return destination;
    }

    public void setDestination(Class destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "RelationShip{" +
                "name='" + name + '\'' +
                ", source=" + source +
                ", destination=" + destination +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelationShip)) return false;
        RelationShip that = (RelationShip) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(source, that.source) &&
                Objects.equals(destination, that.destination);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, source, destination);
    }
}
