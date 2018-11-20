package fr.unice.polytech.graphviz;

import java.util.List;

public class Method {

    private List<Class> classList;

    public Method(List<Class> classList) {
        this.classList = classList;
    }

    public List<Class> getClassList() {
        return classList;
    }

    public void setClassList(List<Class> classList) {
        this.classList = classList;
    }
}
