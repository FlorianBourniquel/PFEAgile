package fr.unice.polytech.cli.warnings;

import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.ClassStatus;
import fr.unice.polytech.repository.DTORepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriticalClassWarning {

    public String check(List<String> ids){
        List<Class> classes = DTORepository.get().getClassesIn(ids);

        List<Class> riskyClasses = new ArrayList<>();

        classes.forEach(aClass -> {
            if (aClass.getClassStatus() != ClassStatus.OK){
                riskyClasses.add(aClass);
            }
        });

        if(riskyClasses.isEmpty()){
            return "This sprint contains no risky classes";
        } else {
            List<String> temp = riskyClasses.stream().map(c -> c.getName() + " (marked as " + c.getClassStatus() + ")").collect(Collectors.toList());
            String sts = temp.stream().collect(Collectors.joining(", "));

            return "This sprint will modify the classes " + sts;
        }
    }

}
