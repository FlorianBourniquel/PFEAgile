package fr.unice.polytech.graphviz;

import java.util.List;

public class Class {

    private List<Method> methodList;

    public Class(List<Method> methodList) {
        this.methodList = methodList;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }
}
