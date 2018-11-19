package fr.unice.polytech.dto;

public class StoryDTO {

    private String text;
    private int number;

    public StoryDTO(String text, int number) {
        this.text = text;
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "StoryDTO{" +
                "text='" + text + '\'' +
                ", number=" + number +
                '}';
    }
}
