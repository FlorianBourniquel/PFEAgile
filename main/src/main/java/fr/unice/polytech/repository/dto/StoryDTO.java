package fr.unice.polytech.repository.dto;

import org.neo4j.driver.v1.Value;

import java.util.Objects;

public class StoryDTO {

    private String text;
    private String name;
    private int businessValue;
    private int storyPoints;

    public StoryDTO(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public StoryDTO(Value value) {
        this.name = value.get("name").asString();
        this.text = value.get("text").asString();
        this.businessValue = value.get("business_value").asInt();
        this.storyPoints = value.get("story_points").asInt();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return Integer.valueOf(name.replace("US",""));
    }

    @Override
    public String toString() {
        return "[" + getNumber() + "] " + text;
    }

    public int getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(int businessValue) {
        this.businessValue = businessValue;
    }

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoryDTO)) return false;
        StoryDTO storyDTO = (StoryDTO) o;
        return businessValue == storyDTO.businessValue &&
                storyPoints == storyDTO.storyPoints &&
                Objects.equals(text, storyDTO.text) &&
                Objects.equals(name, storyDTO.name);
    }

}
