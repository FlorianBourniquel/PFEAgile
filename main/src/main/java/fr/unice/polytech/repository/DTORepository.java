package fr.unice.polytech.repository;

import fr.unice.polytech.graphviz.*;
import fr.unice.polytech.repository.dto.SprintDTO;
import fr.unice.polytech.repository.dto.SprintStatDTO;
import fr.unice.polytech.repository.dto.SprintWithStoriesDTO;
import fr.unice.polytech.repository.dto.StoryDTO;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DTORepository {

    private Db db;

    public DTORepository(Db db) {
        this.db = db;
    }

    public void executeQuery(final String query ){
        this.db.executeQuery(query);
    }

    public SprintWithStoriesDTO getSprintWithStories(String sprintName) {
        try (Session session = db.getDriver().session()) {
            String query = " MATCH (sp:Sprint{name:"+sprintName+"})-[:CONTAINS]->(st:Story) RETURN sp,st";

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

    public SprintDTO getSprint(String sprintName) {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run("MATCH (sp:Sprint{name:\""+sprintName+"\"}) RETURN sp"));
            if(!s.hasNext()){
                return  null;
            }
             return new SprintDTO(s.next().get("sp"));
        }
    }


    public List<StoryDTO> getStoriesIn(List<String> storyNames) {
        try (Session session = db.getDriver().session()) {
            String names =  storyNames.stream().map(x -> "'"+x+"'").collect(Collectors.joining(","));
            StatementResult s = session.writeTransaction(
                    tx -> tx.run("WITH ["+names+"] as names\n" +
                            "MATCH (s:Story)\n" +
                            "WHERE s.name in names\n" +
                            "RETURN s"));
            return  s.list( x -> new StoryDTO(x.get("s")));
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


    public List<SprintStatDTO> getAllSprintStat() {
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(
                    tx -> tx.run(
                            "MATCH (spr:Sprint)-[CONTAINS]->(s:Story)\n" +
                               "RETURN sum(s.business_value) as bv, sum(s.story_points) as sp, spr"));
            return s.list( r -> new SprintStatDTO(r.get("bv").asInt(), r.get("sp").asInt(), new SprintDTO(r.get("spr"))));
        }
    }




    public List<StoryDTO> getBacklog(){
        try (Session session = db.getDriver().session()) {
            StatementResult s = session.writeTransaction(tx -> tx.run("MATCH (s:Story) WHERE NOT (s)<-[:CONTAINS]-(:Sprint) return s"));
            return s.list(r -> new StoryDTO(r.get("s")));
        }
    }


    public Sprint getSprintWithUserStories(String sprintName){

       SprintDTO sprintDTO = getSprint(sprintName);
       if(sprintDTO == null){
           return null;
       }
       Sprint sprint = new Sprint(new ArrayList<>(),sprintName);
       sprint.fill(this.db.getDriver().session());
       return sprint;
    }


    public List<UserStory> getBacklogUserStories(){
        List<StoryDTO> backlog = getBacklog();
        Session session = db.getDriver().session();

        List<UserStory> userStories = backlog.stream()
                        .map(x -> new UserStory(new ArrayList<>(), new ArrayList<>(), x.getName()))
                        .collect(Collectors.toList());

        userStories.forEach(x -> x.fill(session));
        return userStories;
    }

}
