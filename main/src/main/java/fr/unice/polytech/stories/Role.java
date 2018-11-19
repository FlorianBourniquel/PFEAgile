package fr.unice.polytech.stories;

public class Role extends Node{

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Role{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public String toNode(String var) {
        return  "("+var+":Role {name: \""+ text + "\"})";
    }

    @Override
    public String createQuery() {
        return "CREATE "+ toNode();
    }
}
