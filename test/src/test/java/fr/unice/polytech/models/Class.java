package fr.unice.polytech.models;

import org.w3c.dom.Element;

public class Class  {

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
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                '}';
    }
}
