package fr.unice.polytech;

import fr.unice.polytech.cli.commands.Bye;
import fr.unice.polytech.cli.commands.InitBacklog;
import fr.unice.polytech.cli.framework.Shell;
import fr.unice.polytech.environment.Environment;
import org.semanticweb.owlapi.model.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
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
                InitBacklog.class
        );

    }



    public static void main( String[] args ) throws IOException, URISyntaxException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, InterruptedException {

        Main main = new Main();
        main.run();
    }


}
