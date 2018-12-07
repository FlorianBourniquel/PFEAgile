package fr.unice.polytech.graphviz;

import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sprint extends Node{

    private List<UserStory> storyList;
    private Sprint nextSprint;

    public Sprint(String name) {
        super(name);

        this.storyList = new ArrayList<>();
        colorEnum = ColorEnum.SPRINT;
    }

    @Override
    public void fill(Session session) {
        StatementResult findStories = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (s)<-[:CONTAINS]-(n:Sprint {name:\"" + this.getName() + "\"}) RETURN s"));

        this.storyList = findStories.list(story -> new UserStory(story.get("s").get("name").asString()));

        for (UserStory story : this.storyList) {
            story.fill(session);
        }

        StatementResult findNextSprint = session.writeTransaction(
                tx -> tx.run(
                        "MATCH (s)<-[:NEXT]-(n:Sprint {name:\"" + this.getName() + "\"}) RETURN s"));
        if (findNextSprint.hasNext()) {
            this.nextSprint = findNextSprint.list(nextSprint -> new Sprint(nextSprint.get("s").get("name").asString())).get(0);
            nextSprint.fill(session);
        }
    }

    public List<UserStory> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<UserStory> storyList) {
        this.storyList = storyList;
    }

    public Optional<Class> containsDomainElement(Class classWanted) {
        for (UserStory story : this.storyList) {
            return story.getClasses().stream().filter(c -> c.equals(classWanted)).findFirst();
        }

        return Optional.empty();
    }

    public Optional<Method> containsDomainElement(Method methodWanted) {
        for (UserStory story : this.storyList) {
            return story.getMethods().stream().filter(s -> s.equals(methodWanted)).findFirst();
        }

        return Optional.empty();
    }

    public int calculateTotalStoryPoints(){
        return this.storyList
                .stream()
                .mapToInt(UserStory::getStoryPoints)
                .sum();
    }

    public int calculateTotalBusinessValue(){
        return this.storyList
                .stream()
                .mapToInt(UserStory::getBusinessValue)
                .sum();
    }

    public Sprint getNextSprint() {
        return nextSprint;
    }

    public void setNextSprint(Sprint nextSprint) {
        this.nextSprint = nextSprint;
    }
}
