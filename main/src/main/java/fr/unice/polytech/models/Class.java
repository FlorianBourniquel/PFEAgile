package fr.unice.polytech.models;

import fr.unice.polytech.stories.Node;
import org.w3c.dom.Element;


public class Class extends Node {

    private String name;

    public Class(Element node) {
        this.name = node.getAttribute("rdf:about").split("#")[1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toNode(String var) {
        return "("+var+":Class{name:\""+name+"\", status:\"OK\"})";
    }

    @Override
    public String createQuery() {
        return  "MERGE " + toNode();
    }



}
