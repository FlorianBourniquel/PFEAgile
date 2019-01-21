package fr.unice.polytech.graphviz;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserStory extends Node{


    private List<Class> classes;

    private List<Method> methods;

    private int businessValue;

    private int storyPoints;

    private String text;

    public UserStory(String name) {
        super(name);
        this.classes = new ArrayList<>();
        this.methods = new ArrayList<>();
        colorEnum = ColorEnum.USERSTORY;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(int businessValue) {
        this.businessValue = businessValue;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public float getAgileRatio(){
        return (float) businessValue/ (float) storyPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserStory)) return false;
        UserStory userStory = (UserStory) o;
        return businessValue == userStory.businessValue &&
                storyPoints == userStory.storyPoints &&
                Objects.equals(name, userStory.name);
    }

    @Override
    public String toString() {
        return "[" + getNumber() + "] " + text;
    }

    public int getNumber() {
        return Integer.valueOf(name.replace("US",""));
    }

    public String toStringWithRatio() {
        return "["+getNumber()+"]"+ "  [ BV: " + businessValue+", SP: "+storyPoints+", Ratio: " + getAgileRatio() +" ] " + text;
    }
}
