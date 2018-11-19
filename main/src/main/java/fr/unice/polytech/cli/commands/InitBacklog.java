package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;


public class InitBacklog extends Command<Environment> {

	@Override
	public String identifier() { return "init_backlog"; }

	@Override
	public void execute() throws IOException, OWLOntologyStorageException, ParserConfigurationException, URISyntaxException, SAXException, OWLOntologyCreationException, InterruptedException {
		String command = "echo yo";
		String output = executeCommand(command);
		System.out.println(output);

        /*
		List<Path> filesInFolder = Files.walk(Paths.get("/data"))
				.filter(Files::isRegularFile)
				.collect(Collectors.toList());

		List<Path> stories = filesInFolder.stream()
				.filter(x -> x.toUri().getPath().contains("System-user_stories"))
				.sorted(Comparator.comparing(Path::getFileName))
				.collect(Collectors.toList());

		List<File> models = filesInFolder.stream()
				.filter(x -> !x.toUri().getPath().contains("System-user_stories"))
				.sorted(Comparator.comparing(Path::getFileName))
				.map(Path::toFile)
				.collect(Collectors.toList());

		Inserter inserter = new Inserter();

		for (int i = 0; i < stories.size(); i++) {
			inserter.insert(stories.get(i),models.get(i),i);
		}
		System.out.println("Im done");

		*/

	}

	@Override
	public String describe() {
		return "init backlog";
	}

	@Override
	public boolean shouldContinue() { return true; }

}
