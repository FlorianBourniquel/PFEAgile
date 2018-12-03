package fr.unice.polytech;

import fr.unice.polytech.models.Class;
import fr.unice.polytech.models.RelationShip;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AppTest extends TestCase
{

    public AppTest( String testName )
    {
        super( testName );
    }
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }


    public void testApp() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException
    {
        //System.out.println(executeCommand("./../scripts/parse_stories_multi.sh"));

        List<Path> filesInFolder = Files.walk(Paths.get("/usr/src/app/output/System/ontology"))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());

        List<File> models = filesInFolder.stream()
                .map(Path::toFile)
                .collect(Collectors.toList());
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology ontologyDocument = manager.loadOntologyFromOntologyDocument(models.get(0));
        OutputStream out = new FileOutputStream("temp.txt",false);
        manager.saveOntology(ontologyDocument, new RDFXMLDocumentFormat(), out);
        out.close();

        InputStream in = new FileInputStream("temp.txt");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("owl:Class");
        List<Class> classes = new ArrayList<>();
        for (int i =0; i < nodeList.getLength(); i++) {
            classes.add(new Class((Element) nodeList.item(i)));
        }

        nodeList = doc.getElementsByTagName("owl:ObjectProperty");
        List<RelationShip> relationShips = new ArrayList<>();
        for (int i =0; i < nodeList.getLength(); i++) {
            relationShips.add(new RelationShip(nodeList.item(i),classes));
        }

        classes.forEach(System.out::println);
        relationShips.forEach(System.out::println);


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
}
