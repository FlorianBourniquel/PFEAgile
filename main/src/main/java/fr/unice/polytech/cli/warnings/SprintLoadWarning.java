package fr.unice.polytech.cli.warnings;

import fr.unice.polytech.graphviz.Sprint;
import fr.unice.polytech.graphviz.UserStory;
import fr.unice.polytech.repository.DTORepository;

import java.util.List;

public class SprintLoadWarning {

    public String check(List<String> ids, List<UserStory> backlog){
        List<Sprint> list = DTORepository.get().getAllSprints();

        if(list.size() > 0) {
            Double averagePrevPrintsStoryPoints = list.stream()
                    .mapToInt(Sprint::calculateTotalStoryPoints).average().getAsDouble();

            int averageNewSprintStoryPoints = backlog
                    .stream().filter(x -> ids.contains(x.getName()))
                    .mapToInt(UserStory::getStoryPoints).sum();

            if (averageNewSprintStoryPoints > averagePrevPrintsStoryPoints) {
                return "[WARNING] this sprint has more story points (" + averageNewSprintStoryPoints + ") than the average (" + averagePrevPrintsStoryPoints + ")";
            } else {
                return "This sprint contains the average story points from all the other sprints";
            }
        } else {
            return "This sprint is the first one, can't establish sprint load stats";
        }
    }

}
