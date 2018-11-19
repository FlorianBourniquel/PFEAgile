package fr.unice.polytech;

import fr.unice.polytech.cli.commands.Bye;
import fr.unice.polytech.cli.commands.InitBacklog;
import fr.unice.polytech.cli.framework.Shell;
import fr.unice.polytech.env.Env;
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
public class Main  extends Shell<Env>
{

    public Main(){
        this.system = new Env();
        this.invite  = "Agile";
        register(
                Bye.class,
                InitBacklog.class
        );

    }



    public static void main( String[] args ) throws IOException, URISyntaxException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, InterruptedException {

        Main main = new Main();
        main.run();
    }


}
