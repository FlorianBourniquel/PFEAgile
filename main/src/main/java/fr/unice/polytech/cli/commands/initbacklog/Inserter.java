package fr.unice.polytech.cli.commands.initbacklog;

import com.google.gson.Gson;
import fr.unice.polytech.Db;
import fr.unice.polytech.models.Class;
import fr.unice.polytech.models.RelationShip;
import fr.unice.polytech.stories.Story;
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
import java.util.List;

public class Inserter {

    private Db db;
    private OWLOntologyManager manager;

    public Inserter(Db db) {
        manager = OWLManager.createOWLOntologyManager();
        this.db = db;
    }


    public void insert(Path storiesFile, File modelsFile, int storyNumber, StoryEntry storyEntry) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException {


        String data = new String(Files.readAllBytes(storiesFile));
        Story story = new Gson().fromJson(data, Story.class);
        story.setNumber(storyNumber);
        story.setStoryPoints(storyEntry.getStoryPoints());
        story.setBusinessValue(storyEntry.getBusinessValue());

        OWLOntology ontologyDocument = manager.loadOntologyFromOntologyDocument(modelsFile);
        OutputStream out = new FileOutputStream("temp.txt",false);
        manager.saveOntology(ontologyDocument, new RDFXMLDocumentFormat(), out);
        out.close();

        InputStream in = new FileInputStream("temp.txt");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("owl:Class");
        List<Class> classes = story.getClasses();
        for (int i =0; i < nodeList.getLength(); i++) {
            classes.add(new Class((Element) nodeList.item(i)));
        }


        nodeList = doc.getElementsByTagName("owl:ObjectProperty");
        List<RelationShip> relationShips = story.getRelationShips();
        for (int i =0; i < nodeList.getLength(); i++) {
            relationShips.add(new RelationShip(nodeList.item(i),classes));
        }

        db.executeQuery(story.createQuery());
        manager.removeOntology(ontologyDocument);

    }




}
