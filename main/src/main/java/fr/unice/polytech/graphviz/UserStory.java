package fr.unice.polytech.graphviz;

import java.util.List;

public class UserStory extends Node{

    private List<Class> classes;

    private List<Method> methods;

    public UserStory(List<Class> classes, List<Method> methods, String name) {
        super(name);
        this.classes = classes;
        this.methods = methods;
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
}
