package fr.unice.polytech.repository.dto;

public class SprintStatDTO {

    private int businessValue;
    private int storyPoints;
    private SprintDTO sprint;


    public SprintStatDTO(int businessValue, int storyPoints, SprintDTO sprint) {
        this.businessValue = businessValue;
        this.storyPoints = storyPoints;
        this.sprint = sprint;
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

    public SprintDTO getSprint() {
        return sprint;
    }

    public void setSprint(SprintDTO sprint) {
        this.sprint = sprint;
    }

    @Override
    public String toString() {
        return "SprintStatDTO{" +
                "businessValue=" + businessValue +
                ", storyPoints=" + storyPoints +
                ", sprint=" + sprint +
                '}';
    }
}
