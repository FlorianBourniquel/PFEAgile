package fr.unice.polytech.repository;

import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.Method;
import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.dto.SprintDTO;
import fr.unice.polytech.repository.dto.SprintStatDTO;
import fr.unice.polytech.repository.dto.SprintWithStoriesDTO;
import fr.unice.polytech.repository.dto.StoryDTO;
import org.neo4j.driver.v1.Record;
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

    public SprintWithStoriesDTO getSprintWithStories(String sprintName) {
        try (Session session = db.getDriver().session()) {
            String query = " MATCH (sp:Sprint{name:\""+sprintName+"\"})-[:CONTAINS]->(st:Story) RETURN sp,st";

            StatementResult s = session.writeTransaction(tx -> tx.run(query));
            if(!s.hasNext()){
                return  null;
            }
            List<Record> records = s.list();
            List<StoryDTO> storyDTOS = records.stream().map(r -> new StoryDTO(r.get("st"))).collect(Collectors.toList());
            SprintDTO sprintDTO = new SprintDTO(records.get(0).get("sp"));
            return new SprintWithStoriesDTO(sprintDTO,storyDTOS);
        }
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

    public SprintStatDTO getSprintStat(String sprintName) {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (spr:Sprint{name:\""+sprintName+"\"})-[CONTAINS]->(s:Story)\n" +
                                    "RETURN sum(s.business_value) as bv, sum(s.story_points) as sp, spr"));
            Record r = s.next();
            SprintDTO spr = new SprintDTO(r.get("spr"));
            return  new SprintStatDTO(r.get("bv").asInt(), r.get("sp").asInt(), spr);
        }
    }


    public List<Sprint> getAllSprints() {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (spr:Sprint)-[CONTAINS]->(s:Story)\n" +
                               "RETURN spr"));
            return s.list( r -> {
                Sprint res = new Sprint(r.get("spr").get("name").asString());
                res.fill(db.getDriver().session());
                return res;
            });
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

}
