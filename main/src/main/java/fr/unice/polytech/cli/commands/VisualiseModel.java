package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;

import java.io.IOException;

public class VisualiseModel extends Command<Environment> {

    @Override
    public String identifier() {
        return "visualise_domain";
    }

    @Override
    public void execute() throws IOException {
        Parser.parseSprints(DTORepository.get().getAllSprints(),"/data/node.csv","/data/edge.csv");
    }

    @Override
    public String describe() {
        return "Create a visualisation of the current model domain";
    }

}
