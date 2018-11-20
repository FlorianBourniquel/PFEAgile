package fr.unice.polytech.repository.dto;

public class SprintStatDTO {


    private int businessValue;
    private int storyPoints;

    public SprintStatDTO(int businessValue, int storyPoints) {
        this.businessValue = businessValue;
        this.storyPoints = storyPoints;
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
    public String toString() {
        return "SprintStat{" +
                "businessValue=" + businessValue +
                ", storyPoints=" + storyPoints +
                '}';
    }
}
