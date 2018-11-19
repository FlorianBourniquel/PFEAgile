package fr.unice.polytech.stories;

public class StoryMean extends Node{

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "StoryMean{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public String toNode(String var) {
        return "("+var+":Mean {text: \""+ text + "\"})";
    }

    @Override
    public String createQuery() {
        return "CREATE "+ toNode();
    }
}
