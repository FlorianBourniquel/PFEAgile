package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.env.Env;


public class Bye extends Command<Env> {

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
