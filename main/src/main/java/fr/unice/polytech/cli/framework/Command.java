package fr.unice.polytech.cli.framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public abstract class Command<T> {

	abstract public String identifier();
	abstract public void execute() throws Exception;
	abstract public String describe();

	public boolean shouldContinue() { return true; }  // default implementation
	public void load(List<String> args) {  }          // default implementation


	protected Shell<T> shell;

	public void withShell(Shell<T> shell) { this.shell = shell;   }

	public boolean process(List<String> args) throws Exception {
		try { load(args); }
		catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		execute();
		return shouldContinue();
	}

	protected String executeCommand(String command) {
		StringBuilder output = new StringBuilder();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}

	protected void print( Object o){
		System.out.println("\n" + o + "\n");
	}

}
