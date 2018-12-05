package fr.unice.polytech.repository;

import fr.unice.polytech.graphviz.*;
import fr.unice.polytech.graphviz.Class;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;

import java.util.List;
import java.util.stream.Collectors;

public class DTORepository {

    private static Db db;

    private static DTORepository instance;

    public static DTORepository get(){
        if(instance == null){
           instance = new DTORepository(new Db());
        }
        return instance;
    }

    private DTORepository(Db db) {
        DTORepository.db = db;
    }

    public Db getDb() {
        return db;
    }

    public void executeQuery(final String query ){
        db.executeQuery(query);
    }

    public Sprint getSprint(String sprintName) {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run("MATCH (sp:Sprint{name:\""+sprintName+"\"}) RETURN sp"));
            if(!s.hasNext()){
                return null;
            }
            return new Sprint(s.next().get("sp").get("name").asString());
        }
    }


    public List<UserStory> getStoriesIn(List<String> storyNames) {
        try (Session session = db.getDriver().session()) {
            String names =  storyNames.stream().map(x -> "'"+x+"'").collect(Collectors.joining(","));
            StatementResult s = session.writeTransaction(
                    tx -> tx.run("WITH ["+names+"] as names\n" +
                            "MATCH (s:Story)\n" +
                            "WHERE s.name in names\n" +
                            "RETURN s"));
            return  s.list( x -> this.createUserStoryFromRequest(x.get("s")));
        }
    }

    public List<Class> getClassesIn(List<String> classesNames) {
        try (Session session = db.getDriver().session()) {
            String names =  classesNames.stream().map(x -> "'"+x+"'").collect(Collectors.joining(","));

            StatementResult s = session.writeTransaction(
                    tx -> tx.run("WITH ["+names+"] as names\n" +
                            "MATCH (s:Story), (c:Class)\n" +
                            "WHERE s.name in names\n" +
                            "AND (s)-[:INVOLVES]->(c)\n" +
                            "RETURN c"));
            return s.list( x -> this.createClassFromRequest(x.get("c")));
        }
    }

    public List<Sprint> getAllSprints() {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (spr:Sprint) RETURN spr"));
            return s.list( r -> {
                Sprint res = new Sprint(r.get("spr").get("name").asString());
                this.fill(res);

                res.getStoryList().forEach(story -> {
                    this.fill(story);

                    story.getMethods().forEach(this::fill);
                    story.getClasses().forEach(this::fill);
                });

                return res;
            });
        }
    }

    public void changeStatus(String className, ClassStatus classStatus) {
        try (Session session = db.getDriver().session()) {
            session.writeTransaction(
                    tx -> tx.run("MATCH (c:Class {name:\"" + className + "\"}) SET c.status=\"" + classStatus.name() + "\""));
        }
    }

    public List<UserStory> getBacklog(){
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(tx -> tx.run("MATCH (s:Story) WHERE NOT (s)<-[:CONTAINS]-(:Sprint) return s"));
            return s.list(r -> createUserStoryFromRequest(r.get("s")));
        }
    }

    private UserStory createUserStoryFromRequest(Value value){
        UserStory userStory = new UserStory(value.get("name").asString());
        userStory.setBusinessValue(value.get("business_value").asInt());
        userStory.setStoryPoints(value.get("story_points").asInt());
        userStory.setText(value.get("text").asString());
        return userStory;
    }

    private Class createClassFromRequest(Value classValue) {
        Class res = new Class(classValue.get("name").asString());
        res.setClassStatus(ClassStatus.valueOf(classValue.get("status").asString()));
        return res;
    }

    public Sprint getSprintWithUserStories(String sprintName){
       Sprint sprint = getSprint(sprintName);
       if(sprint == null){
           return null;
       }

       this.fill(sprint);
       return sprint;
    }


    public List<UserStory> getBacklogUserStories(){
        List<UserStory> backlog = getBacklog();

        backlog.forEach(this::fill);
        return backlog;
    }

    //Visitor fill

    public void fill(Sprint sprint) {
        StatementResult findStories = db.getDriver().session().writeTransaction(
                tx -> tx.run(
                        "MATCH (s)<-[:CONTAINS]-(n:Sprint {name:\"" + sprint.getName() + "\"}) RETURN s"));

        sprint.setStoryList(findStories.list(story -> createUserStoryFromRequest(story.get("s"))));
    }

    public void fill(UserStory story){
        StatementResult findClasses = this.getDb().getDriver().session().writeTransaction(
                tx -> tx.run(
                        "MATCH (c:Class)<-[:INVOLVES]-(n:Story {name:\"" + story.getName() + "\"}) RETURN c"));

        story.setClasses(findClasses.list(classElement -> new Class(classElement.get("c").get("name").asString())));

        StatementResult findMethods = this.getDb().getDriver().session().writeTransaction(
                tx -> tx.run(
                        "MATCH (c:RelationShip)<-[:INVOLVES]-(n:Story {name:\"" + story.getName() + "\"}) RETURN c"));

        story.setMethods(findMethods.list(methodElement -> new Method(methodElement.get("c").get("name").asString())));
    }

    public void fill(Class toFill) {
        StatementResult findRelationShip = this.getDb().getDriver().session().writeTransaction(
                tx -> tx.run(
                        "MATCH (r:RelationShip)<-[:CAN]-(n:Class {name:\"" + toFill.getName() + "\"}) RETURN r"));

        toFill.setMethodList(findRelationShip.list(methodElement -> new Method(methodElement.get("r").get("name").asString())));
    }

    public void fill(Method toFill){
        StatementResult findRelationShip = this.getDb().getDriver().session().writeTransaction(
                tx -> tx.run(
                        "MATCH (r:Class)<-[:TARGET]-(n:RelationShip {name:\"" + toFill.getName() + "\"}) RETURN r"));

        toFill.setClassList(findRelationShip.list(classElement -> new Class(classElement.get("r").get("name").asString())));
    }

    public UserStory getUSByName(String wantedUS) {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(tx -> tx.run("MATCH (s:Story {name:\"" + wantedUS +"\"}) return s"));

            UserStory story = createUserStoryFromRequest(s.next().get("s"));

            this.fill(story);

            story.getMethods().forEach(this::fill);
            story.getClasses().forEach(this::fill);

            return story;
        }
    }

    public void removeUSFromBacklog(String remove) {
        try (Session session = db.getDriver().session()) {
            session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (s:Story {name:\"" + remove + "\"})-[r]-(a)\n" +
                                    "WHERE SIZE(()-[:INVOLVES]->(a)) < 2\n" +
                                    "AND SIZE(()-[:HAS_FOR_ROLE]->(a)) < 2\n" +
                                    "DETACH DELETE s,a")
            );
        }
    }
}
