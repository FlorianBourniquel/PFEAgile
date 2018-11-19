package fr.unice.polytech.models;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;

public class RelationShip extends fr.unice.polytech.stories.Node {

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
    public String toNode(String var) {
        return "("+var+":RelationShip{name:\""+name+"\"})";
    }



    @Override
    public String createQuery() {
        return "MATCH "+ source.toNode("w") +
               "MATCH "+ destination.toNode("d") +
               "CREATE"+ toNode("r") +
               "MERGE (w)-[:CAN]->(r)-[:TARGET]->(d)";
    }

    public String createQuery(String relationShipAlias, String sourceAlias, String destAlias,String storyAlias) {
        return  " CREATE"+ toNode(relationShipAlias) + "\n"+
                " MERGE ("+sourceAlias+")-[:CAN]->("+relationShipAlias+")-[:TARGET]->("+destAlias+")" +
                " MERGE ("+storyAlias+")-[:INVOLVES]->("+relationShipAlias+")";
    }


}
