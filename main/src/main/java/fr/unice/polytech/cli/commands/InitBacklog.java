package fr.unice.polytech.cli.commands;

import fr.unice.polytech.StreamGobbler;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;


public class InitBacklog extends Command<Environment> {

	@Override
	public String identifier() { return "init_backlog"; }

	@Override
	public void execute() throws IOException, OWLOntologyStorageException, ParserConfigurationException, URISyntaxException, SAXException, OWLOntologyCreationException, InterruptedException {


        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", "echo","yo");
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        System.out.println(exitCode);

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
