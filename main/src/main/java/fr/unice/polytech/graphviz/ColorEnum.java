package fr.unice.polytech.graphviz;

public enum ColorEnum {

    ADDED("GREEN"),
    MODIFIED("ORANGE"),
    REMOVED("RED"),
    DEFAULT("GREY");

    private String color;

    ColorEnum(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
