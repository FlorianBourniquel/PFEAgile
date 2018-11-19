package fr.unice.polytech;

import fr.unice.polytech.cli.commands.Bye;
import fr.unice.polytech.cli.commands.InitBacklog;
import fr.unice.polytech.cli.commands.ListStories;
import fr.unice.polytech.cli.framework.Shell;
import fr.unice.polytech.environment.Environment;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class Main  extends Shell<Environment>
{

    public Main(){
        this.system = new Environment();
        this.invite  = "Agile";
        register(
                Bye.class,
                InitBacklog.class,
                ListStories.class
        );

    }



    public static void main( String[] args ) throws IOException, URISyntaxException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, InterruptedException {
        Thread.sleep(10000);
        Main main = new Main();
        main.run();
    }


}
