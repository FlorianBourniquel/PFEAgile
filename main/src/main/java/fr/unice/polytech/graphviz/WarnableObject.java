package fr.unice.polytech.graphviz;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class WarnableObject {

    private List<String> warnings;

    public WarnableObject(){
        this.warnings = new ArrayList<>();
    }

    public void addWarning(String warning){
        this.warnings.add(warning);
    }

    public String printWarnings(){
        return "\n\n[WARNINGS]\n" + warnings.stream().collect(Collectors.joining("\n"));
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
