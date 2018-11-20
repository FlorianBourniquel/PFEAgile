package fr.unice.polytech.graphviz;

import java.util.List;

public class Method extends Node {

    private List<Class> classList;

    public Method(List<Class> classList, String name) {
        super(name);
        this.classList = classList;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }
}
