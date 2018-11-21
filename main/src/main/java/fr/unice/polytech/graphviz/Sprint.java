package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;

public class Sprint extends Node{

    private List<UserStory> storyList;

    public Sprint(List<UserStory> storyList, String name) {
        super(name);

        this.storyList = storyList;
        colorEnum = ColorEnum.SPRINT;
    }

    @Override
    public void fill(Session session) {
        StatementResult findStories = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (s)<-[:CONTAINS]-(n:Sprint {name:\"" + this.getName() + "\"}) RETURN s"));

        this.storyList = findStories.list(story -> new UserStory(new ArrayList<>(), new ArrayList<>(), story.get("s").get("name").asString()));
    }

    public List<UserStory> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<UserStory> storyList) {
        this.storyList = storyList;
    }
}
