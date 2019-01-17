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

        relsFromMulti = relsFromMulti.stream().distinct().sorted(Comparator.comparing(RelationShip::getName)).collect(Collectors.toList());
        relsFromSingle = relsFromSingle.stream().distinct().sorted(Comparator.comparing(RelationShip::getName)).collect(Collectors.toList());


        //CLasses lost from single parsing
        List<Class> finalClassesFromMulti = classesFromMulti;
        List<Class> lostFromSingle = classesFromSingle
                .stream()
                .filter(x -> !finalClassesFromMulti.contains(x)).collect(Collectors.toList());
        int percentage = (int) (( (float) lostFromSingle.size() / (float) classesFromSingle.size() ) *100);
        System.out.println("\nClasses lost from single parsing[ " + percentage + "% ]");
        lostFromSingle.forEach(x -> {
            final int minlev = minimalLevenshteinDistance(x, finalClassesFromMulti);
            final boolean isComposed = finalClassesFromMulti.stream().anyMatch(a -> x.getName().toLowerCase().endsWith(a.getName().toLowerCase()));
            System.out.println( "\t" + x + " :  Minimun Levenshtein Distance = "+ String.valueOf(minlev) + " : isComposed  = " + String.valueOf(isComposed));
        });

        //Classes in multi parsing but not in single parsing
        List<Class> finalClassesFromSingle = classesFromSingle;
        List<Class> inMultiNotInSingle = classesFromMulti
                .stream()
                .filter(x -> !finalClassesFromSingle.contains(x)).collect(Collectors.toList());
        percentage = (int) (( (float) inMultiNotInSingle.size() / (float) classesFromMulti.size() ) *100);
        System.out.println("\nClasses in multi but not in single parsing [ " + percentage + "% ]");
        inMultiNotInSingle.forEach(x -> System.out.println( "\t" + x + " :  Minimun Levenshtein Distance = "+ String.valueOf(minimalLevenshteinDistance(x, finalClassesFromSingle))));

        //Relationships lost from single parsing
        List<RelationShip> finalRelsFromMulti = relsFromMulti;
        List<RelationShip> relsLostFromSingle = relsFromSingle
                .stream()
                .filter(r -> !finalRelsFromMulti.contains(r)).collect(Collectors.toList());
        percentage = (int) (( (float) relsLostFromSingle.size() / (float) relsFromSingle.size() ) *100);
        System.out.println("\nRelationShips lost from single [ " + percentage + "% ]");
        relsLostFromSingle.forEach(x -> System.out.println( "\t" + x + " :  Minimun Levenshtein Distance = "+ String.valueOf(minimalLevenshteinDistance(x, finalRelsFromMulti))));



        List<RelationShip> finalRelsFromSingle = relsFromSingle;
        List<RelationShip> relsInMultiNotInSingle = relsFromMulti
                .stream()
                .filter( r -> ! finalRelsFromSingle.contains(r))
                .collect(Collectors.toList());
        percentage = (int) (( (float) relsInMultiNotInSingle.size() / (float) relsFromMulti.size() ) *100);
        System.out.println("\nRelationShip in multi but not in single [ " + percentage + "% ]");

        relsInMultiNotInSingle.forEach(x -> System.out.println( "\t" +x + " :  Minimun Levenshtein Distance = "+ String.valueOf(minimalLevenshteinDistance(x, finalRelsFromSingle))));

        System.out.println("\n");



    }


    private void parseMulti(List<Class> classes, List<RelationShip> rels) throws IOException, OWLOntologyCreationException, OWLOntologyStorageException, ParserConfigurationException, SAXException {
        System.out.println(executeCommand("./../scripts/parse_stories_multi.sh"));

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
        //models.remove(models.size() -1);// TODO enlever quand on decommente l'execution des commandes
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


    private int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }


    private int minimalLevenshteinDistance(RelationShip relationShip, List<RelationShip> relationShips) {
        return relationShips.stream()
                .mapToInt(x -> levenshteinDistance(x.getName(), relationShip.getName()))
                .min().getAsInt();
    }


    private int minimalLevenshteinDistance(Class clazz, List<Class> clazzes) {
        return clazzes.stream()
                .mapToInt(x -> levenshteinDistance(x.getName(), clazz.getName()))
                .min().getAsInt();
    }
}
