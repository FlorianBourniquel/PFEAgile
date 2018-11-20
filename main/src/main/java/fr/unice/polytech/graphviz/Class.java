package fr.unice.polytech.graphviz;

import java.util.List;

public class Class extends Node {

    private List<Method> methodList;

    public Class(List<Method> methodList, String name) {
        super(name);
        this.methodList = methodList;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }
}
