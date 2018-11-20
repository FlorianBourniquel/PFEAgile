package fr.unice.polytech.graphviz;

import java.util.List;

public class Sprint extends Node{

    private List<UserStory> storyList;

    public Sprint(List<UserStory> storyList, String name) {
        super(name);

        this.storyList = storyList;
    }

    public List<UserStory> getStoryList() {
        return storyList;
    }

    public void setStoryList(List<UserStory> storyList) {
        this.storyList = storyList;
    }
}
