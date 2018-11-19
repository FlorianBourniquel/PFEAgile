package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;


public class Bye extends Command<Environment> {

	@Override
	public String identifier() { return "bye"; }

	@Override
	public void execute() { }

	@Override
	public String describe() {
		return "Exit Agile Backlog";
	}

	@Override
	public boolean shouldContinue() { return false; }

}
