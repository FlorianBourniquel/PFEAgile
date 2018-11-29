package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.framework.Command;
import fr.unice.polytech.environment.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractSprintCommand extends Command<Environment> {

    ArrayList<String> storyIds;
    String sprintName;

    public AbstractSprintCommand(){
        this.sprintName = "";
        this.storyIds = new ArrayList<>();
    }

    @Override
    public void load(List<String> args){
        this.loadBasic(args, getNbOtherArgs());
    }

    protected abstract int getNbOtherArgs();

    private void loadBasic(List<String> args, int nbOtherArgs){
        initCmd();

        if(args.size() > nbOtherArgs) {
            sprintName = args.get(0);
            args.remove(0);

            Iterator<String> argsIteration = args.iterator();

            int skipped = 0;

            while(skipped < nbOtherArgs){
                argsIteration.next();
                skipped++;
            }

            while (argsIteration.hasNext()){
                this.storyIds.add(argsIteration.next());
                argsIteration.remove();
            }
        }
    }

    private void initCmd() {
        this.storyIds.clear();
        this.sprintName = "";
    }

    @Override
    public void execute() throws IOException {
        if(this.sprintName.isEmpty()){
            throw new IOException("Please specify a name for the sprint.");
        }
        check();
    }

    protected void check() throws IOException { }

    @Override
    public boolean shouldContinue() { return true; }

}
