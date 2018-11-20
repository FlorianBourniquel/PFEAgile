package fr.unice.polytech;

import fr.unice.polytech.cli.commands.Bye;
import fr.unice.polytech.cli.commands.CreateSprint;
import fr.unice.polytech.cli.commands.initbacklog.InitBacklog;
import fr.unice.polytech.cli.commands.ListStories;
import fr.unice.polytech.cli.commands.*;
import fr.unice.polytech.cli.commands.whatif.WhatIfIAddStory;
import fr.unice.polytech.cli.commands.whatif.WhatIfIRemoveStory;
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
                CreateSprint.class,
                InitBacklog.class,
                ListStories.class,
                VizualiseModel.class,
                WhatIfIAddStory.class,
                WhatIfIRemoveStory.class
        );

    }



    public static void main( String[] args ) throws IOException, URISyntaxException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException, InterruptedException {
        Thread.sleep(10000);
        Main main = new Main();
        main.run();
    }


}
