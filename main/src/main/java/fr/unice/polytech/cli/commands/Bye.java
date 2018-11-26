package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;


public class Bye extends Command<Environment> implements WebCommand {

	@Override
	public String identifier() { return "bye"; }

	@Override
	public Response execResponse() {
		return Response.ok("bye").build();
	}

	@Override
	public void execute() { }

	@Override
	public String describe() {
		return "Exit Agile Backlog";
	}

	@Override
	public boolean shouldContinue() { return false; }

}
