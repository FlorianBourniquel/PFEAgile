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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

       System.out.println("Parsing multi");
        List<Class> classesFromMulti = new ArrayList<>();
        List<RelationShip> relsFromMulti = new ArrayList<>();
        parseMulti(classesFromMulti,relsFromMulti);

        System.out.println("Parsing single");
        List<Class> classesFromSingle = new ArrayList<>();
        List<RelationShip> relsFromSingle = new ArrayList<>();
        parseSingle(classesFromSingle,relsFromSingle);

        classesFromMulti = classesFromMulti.stream().distinct().sorted(Comparator.comparing(Class::getName)).collect(Collectors.toList());
        classesFromSingle = classesFromSingle.stream().distinct().sorted(Comparator.comparing(Class::getName)).collect(Collectors.toList());
        assertEquals(classesFromMulti, classesFromSingle);

        relsFromMulti = relsFromMulti.stream().distinct().sorted(Comparator.comparing(RelationShip::getName)).collect(Collectors.toList());
        relsFromSingle = relsFromSingle.stream().distinct().sorted(Comparator.comparing(RelationShip::getName)).collect(Collectors.toList());
        assertEquals(relsFromMulti,relsFromSingle);

    }


    private void parseMulti(List<Class> classes, List<RelationShip> rels) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException {
        System.out.println(executeCommand("./../scripts/parse_stories_multi.sh"));

        List<File> models =Files.walk(Paths.get("/usr/src/app/output/System/ontology"))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        for (File model : models){

            OWLOntology ontologyDocument = manager.loadOntologyFromOntologyDocument(model);
            OutputStream out = new FileOutputStream("temp.txt",false);
            manager.saveOntology(ontologyDocument, new RDFXMLDocumentFormat(), out);
            manager.removeOntology(ontologyDocument);
            out.close();

            InputStream in = new FileInputStream("temp.txt");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("owl:Class");
            for (int i =0; i < nodeList.getLength(); i++) {
                classes.add(new Class((Element) nodeList.item(i)));
            }

            nodeList = doc.getElementsByTagName("owl:ObjectProperty");
            for (int i =0; i < nodeList.getLength(); i++) {
                rels.add(new RelationShip(nodeList.item(i),classes));
            }
        }
    }



    private void parseSingle(List<Class> classes, List<RelationShip> rels) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException {
        System.out.println(executeCommand("./../scripts/parse_stories_single.sh"));
        Pattern p = Pattern.compile("\\d+");
        List<File> models =Files.walk(Paths.get("/usr/src/app/output/System/ontology"))
                .filter(Files::isRegularFile)
                .sorted(Comparator.comparingInt(o -> {
                    Matcher m = p.matcher(o.getFileName().toString());
                    m.find();
                    return Integer.valueOf(m.group());
                }))

                .map(Path::toFile)
                .collect(Collectors.toList());
        File model = models.get(models.size() -1);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        OWLOntology ontologyDocument = manager.loadOntologyFromOntologyDocument(model);
        OutputStream out = new FileOutputStream("temp.txt",false);
        manager.saveOntology(ontologyDocument, new RDFXMLDocumentFormat(), out);
        manager.removeOntology(ontologyDocument);
        out.close();

        InputStream in = new FileInputStream("temp.txt");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("owl:Class");
        for (int i =0; i < nodeList.getLength(); i++) {
            classes.add(new Class((Element) nodeList.item(i)));
        }

        nodeList = doc.getElementsByTagName("owl:ObjectProperty");
        for (int i =0; i < nodeList.getLength(); i++) {
            rels.add(new RelationShip(nodeList.item(i),classes));
        }

    }


    private String executeCommand(String command) {
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
