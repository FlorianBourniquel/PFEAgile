package fr.unice.polytech.cli.commands;


import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import org.neo4j.driver.v1.Session;

import java.io.IOException;

public class VizualiseModel extends Command<Environment> {

    @Override
    public String identifier() { return "create_sprint"; }

    @Override
    public void execute() throws IOException {
        /*
        try (Session session = shell.system.getDb().getDriver().session()) {
            session.writeTransaction(tx -> tx.run(resBuilder.toString()));
        }
        */
    }

    @Override
    public String describe() {
        return "Create a sprint based on specified stories\n" +
                "      - name:String\n" +
                "      - stories:List<Integer>";
    }
}
