package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VisualiseModelUS extends Command<Environment> {

    private String wantedUS;

    @Override
    public String identifier() {
        return "visualise_domain_us";
    }

    @Override
    public void load(List<String> args){
        if(args.size() > 0){
            wantedUS = args.get(0);
        } else {
            throw new IllegalArgumentException("Please specify a US name");
        }
    }

    @Override
    public void execute() throws IOException {
        Parser.parseUS(Collections.singletonList(DTORepository.get().getUSByName(wantedUS)),"/data/node.csv","/data/edge.csv");
    }

    @Override
    public String describe() {
        return "Create a visualisation of the model domain of a selected User Story";
    }

}
