package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.io.IOException;

public class VisualiseModel extends Command<Environment> implements WebCommand{

    @Override
    public String identifier() {
        return "visualise_domain";
    }

    @Override
    public Response execResponse() throws CmdException {
        try {
            execute();

            return Response.ok().build();
        } catch (IOException e) {
            throw new CmdException(e.getMessage());
        }
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
