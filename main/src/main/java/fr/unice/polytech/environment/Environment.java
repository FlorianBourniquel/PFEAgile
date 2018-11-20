package fr.unice.polytech.environment;

import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.repository.Db;
import fr.unice.polytech.repository.dto.StoryDTO;
import java.util.LinkedList;
import java.util.List;

public class Environment {


    private List<StoryDTO> stories = new LinkedList<StoryDTO>();
    private Db db;
    private DTORepository repository;

    public Environment() {
        this.db = new Db();
        this.repository = new DTORepository(db);
    }

    public List<StoryDTO> getStories() {
        return stories;
    }

    public void setStories(List<StoryDTO> stories) {
        this.stories = stories;
    }

    public Db getDb() {
        return db;
    }

    public DTORepository getRepository() {
        return repository;
    }
}
