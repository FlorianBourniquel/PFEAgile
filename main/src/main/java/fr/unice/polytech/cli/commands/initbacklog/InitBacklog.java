package fr.unice.polytech.cli.commands.initbacklog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class InitBacklog extends Command<Environment> implements WebCommand {

	private String data;

	@Override
	public String identifier() { return "init_backlog"; }

	@Override
	public void load(List<String> args){
		if(args.size() > 0){
			data = args.get(0);
		} else {
			try {
				data = new String(Files.readAllBytes(Paths.get("./output/entries.txt")));
			} catch (IOException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}

	@Override
	public Response execResponse() throws CmdException{
		String response;
		try {
			response = executeWithResponse();
		} catch (IOException | OWLOntologyStorageException | SAXException | ParserConfigurationException | OWLOntologyCreationException | InterruptedException e) {
			e.printStackTrace();

			throw new CmdException("Erreur lors de l'initialisation du backlog");
		}
		return Response.ok(response).build();
	}

	@Override
	public void execute() throws IOException, OWLOntologyStorageException, ParserConfigurationException, SAXException, OWLOntologyCreationException, InterruptedException {
        print(executeWithResponse());
	}

	private String executeWithResponse() throws IOException, OWLOntologyStorageException, ParserConfigurationException, SAXException, OWLOntologyCreationException, InterruptedException {
		List<StoryEntry> entries = new Gson().fromJson(data, new TypeToken<List<StoryEntry>>(){}.getType());
		String vnInput = entries.stream().map(StoryEntry::getText).collect(Collectors.joining("\n")).concat("\n");
		Files.write(Paths.get("./output/stories.txt"), vnInput.getBytes());
		System.out.println("Parsing stories .... this may take a lot of time ...");
		executeCommand("./parse_stories.sh");
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
		Inserter inserter = new Inserter(DTORepository.get());
		for (int i = 0; i < stories.size(); i++) {
			inserter.insert(stories.get(i),models.get(i),i, entries.get(i));
		}
		return "Stories successfuly inserted";
	}

	@Override
	public String describe() {
		return "initialize the backlog";
	}

	@Override
	public boolean shouldContinue() { return true; }

}
