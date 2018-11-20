package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;

import java.util.ArrayList;
import java.util.List;

public class VisualiseImpact extends Command<Environment> {

    private String sprintName;

    private List<Integer> storyIds;

    private boolean addMode;

    public VisualiseImpact(){
        this.sprintName = "";
        this.storyIds = new ArrayList<>();
        this.addMode = true;
    }

    @Override
    public String identifier() {
        return "visualise_impact";
    }

    @Override
    public void execute() {
        if(addMode){
            visualiseAddStories();
        } else {
            visualiseRemoveStories();
        }
    }

    private void visualiseRemoveStories() {

    }

    private void visualiseAddStories() {

    }

    @Override
    public void load(List<String> args) {
        initCmd();

        if(args.size() > 1){
            sprintName = args.get(0);
            args.remove(0);

            addMode = Boolean.valueOf(args.get(0));
            args.remove(0);

            args.forEach(s -> storyIds.add(Integer.valueOf(s)));
        }
    }

    private void initCmd() {
        this.storyIds.clear();
        this.sprintName = "";
    }

    @Override
    public String describe() {
        return "Visualise the impact of a story on the selected sprint\n" +
                "       - name:String\n" +
                "       - addMode:Boolean\n" +
                "       - stories:List<Integer>";
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }
}