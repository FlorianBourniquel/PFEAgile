package fr.unice.polytech.stories;

public abstract class Node {

    public abstract String toNode(String var);

    public String toNode(){
        return toNode("");
    }

    public abstract String createQuery();
}
