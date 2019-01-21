package fr.unice.polytech.graphviz;


public class StoryWithComplexity{
    @Override
    public String toString() {
        return "StoryWithComplexity{" +
                "userStory=" + userStory +
                ", nbClassesAdded=" + nbClassesAdded +
                '}';
    }

    private UserStory userStory;
    private int nbClassesAdded;

    public StoryWithComplexity(UserStory userStory, int nbClassesAdded) {
        this.userStory = userStory;
        this.nbClassesAdded = nbClassesAdded;
    }

    public UserStory getUserStory() {
        return userStory;
    }

    public void setUserStory(UserStory userStory) {
        this.userStory = userStory;
    }

    public int getNbClassesAdded() {
        return nbClassesAdded;
    }

    public void setNbClassesAdded(int nbClassesAdded) {
        this.nbClassesAdded = nbClassesAdded;
    }
}
