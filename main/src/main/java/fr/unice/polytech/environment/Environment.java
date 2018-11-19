package fr.unice.polytech.environment;

import fr.unice.polytech.Db;
import fr.unice.polytech.dto.StoryDTO;

import java.util.LinkedList;
import java.util.List;

public class Environment {


    private List<StoryDTO> stories = new LinkedList<StoryDTO>();
    private Db db = new Db();

    public List<StoryDTO> getStories() {
        return stories;
    }

    public void setStories(List<StoryDTO> stories) {
        this.stories = stories;
    }

    public Db getDb() {
        return db;
    }
}
