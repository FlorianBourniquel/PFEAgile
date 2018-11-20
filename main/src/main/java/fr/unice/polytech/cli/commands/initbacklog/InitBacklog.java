package fr.unice.polytech.cli.commands.initbacklog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class InitBacklog extends Command<Environment> {

	@Override
	public String identifier() { return "init_backlog"; }

	@Override
	public void execute() throws IOException, OWLOntologyStorageException, ParserConfigurationException, URISyntaxException, SAXException, OWLOntologyCreationException, InterruptedException {

        String data = new String(Files.readAllBytes(Paths.get("./output/entries.txt")));
        List<StoryEntry> entries = new Gson().fromJson(data, new TypeToken<List<StoryEntry>>(){}.getType());
        String vnInput = entries.stream().map(StoryEntry::getText).collect(Collectors.joining("\n")).concat("\n");
        Files.write(Paths.get("./output/stories.txt"), vnInput.getBytes());
		String command = "./parse_stories.sh";
        System.out.println("Parsing stories .... this may take a lot of time ...");
		executeCommand(command);
        System.out.println("Inserting  stories into the DB.... this may take a lot of time ...");
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
		Inserter inserter = new Inserter(this.shell.system.getDb());

		for (int i = 0; i < stories.size(); i++) {
			inserter.insert(stories.get(i),models.get(i),i, entries.get(i));
		}
		System.out.println("Stories successfuly inserted");
	}

	@Override
	public String describe() {
		return "init backlog";
	}

	@Override
	public boolean shouldContinue() { return true; }

}
