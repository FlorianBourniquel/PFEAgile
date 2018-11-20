package fr.unice.polytech.repository.dto;

import java.util.List;

public class SprintWithStoriesDTO {

    private SprintDTO sprint;
    private List<StoryDTO> stories;

    public SprintWithStoriesDTO(SprintDTO sprint, List<StoryDTO> stories) {
        this.sprint = sprint;
        this.stories = stories;
    }

    public SprintDTO getSprint() {
        return sprint;
    }

    public void setSprint(SprintDTO sprint) {
        this.sprint = sprint;
    }

    public List<StoryDTO> getStories() {
        return stories;
    }

    public void setStories(List<StoryDTO> stories) {
        this.stories = stories;
    }
    
    
}
