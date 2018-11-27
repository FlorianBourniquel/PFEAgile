package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.repository.DTORepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VisualiseModelSprint extends Command<Environment> {

    private String wantedSprint;

    @Override
    public String identifier() {
        return "visualise_domain_sprint";
    }

    @Override
    public void load(List<String> args){
        if(args.size() > 0){
            wantedSprint = args.get(0);
        } else {
            throw new IllegalArgumentException("Please specify a sprint name");
        }
    }

    @Override
    public void execute() throws IOException {
        Sprint sprint = DTORepository.get().getSprint(wantedSprint);

        sprint.fill(DTORepository.get().getDb().getDriver().session());

        Parser.parseSprints(Collections.singletonList(sprint),"/data/node.csv","/data/edge.csv");
    }

    @Override
    public String describe() {
        return "Create a visualisation of the model domain of a selected sprint";
    }

}