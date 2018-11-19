package fr.unice.polytech;

import org.semanticweb.owlapi.model.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args ) throws IOException, URISyntaxException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, InterruptedException {

        Thread.sleep(25000);
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
    }


}
