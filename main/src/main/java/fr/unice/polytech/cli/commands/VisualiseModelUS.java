package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class VisualiseModelUS extends Command<Environment> implements WebCommand{

    private String wantedUS;

    @Override
    public String identifier() {
        return "visualise_domain_us";
    }

    @Override
    public Response execResponse() throws CmdException {
        try {
            execute();
            executeCommand("Rscript /usr/src/app/output/Script.R");
            return Response.ok().build();
        } catch (IOException e) {
            throw new CmdException(e.getMessage());
        }
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
