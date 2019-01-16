package fr.unice.polytech.models;

import org.w3c.dom.Element;

import java.util.Objects;

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
        return "Class : " + name ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Class)) return false;
        Class aClass = (Class) o;
        return Objects.equals(name, aClass.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}
