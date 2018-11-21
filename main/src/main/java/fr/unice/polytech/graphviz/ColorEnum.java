package fr.unice.polytech.graphviz;

public enum ColorEnum {

    ADDED("#92da4b"),
    MODIFIED("#FFC300"),
    REMOVED("#da4b4b"),
    DEFAULT("#c1c1c1"),
    SPRINT("#da4b4b"),
    USERSTORY("#92da4b"),
    CLASS("#4bdada"),
    METHOD("#924bda");

    private String color;

    ColorEnum(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
