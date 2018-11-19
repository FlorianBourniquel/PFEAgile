package fr.unice.polytech.dto;

public class StoryDTO {

    private String text;
    private String name;

    public StoryDTO(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return Integer.valueOf(name.replace("US",""));
    }

    @Override
    public String toString() {
        return "[" + getNumber() + "] " + text;
    }
}
