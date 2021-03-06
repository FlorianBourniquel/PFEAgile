package fr.unice.polytech.cli.commands;

import fr.unice.polytech.cli.commands.utils.Parser;
import fr.unice.polytech.graphviz.Class;
import fr.unice.polytech.graphviz.*;
import fr.unice.polytech.repository.DTORepository;
import fr.unice.polytech.web.CmdException;
import fr.unice.polytech.web.WebCommand;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class VisualiseImpact extends AbstractSprintCommand implements WebCommand{

    private boolean addMode;

    public VisualiseImpact() {
        this.addMode = true;
    }

    @Override
    public String identifier() {
        return "visualise_impact";
    }

    @Override
    public Response execResponse() throws CmdException {
        try {
            execute();
            executeCommand("Rscript /usr/src/app/output/Script.R");
            return Response.ok().build();
        } catch (IOException e) {
            throw new CmdException(e.getMessage());
        }
    }

    @Override
    public void execute() throws IOException {
        if(addMode){
            visualiseAddStories();
        } else {
            visualiseRemoveStories();
        }
    }

    private void visualiseRemoveStories() throws IOException {
        super.execute();

        Sprint sprint = this.initSprint();

        List<UserStory> stories = DTORepository.get().getStoriesIn(this.storyIds);

        stories.forEach(
                s -> {
                    DTORepository.get().fill(s);
                    sprint.getStoryList().remove(s);

                    for (Class classElement: s.getClasses()) {
                        DTORepository.get().fill(classElement);

                        Optional<Class> classOptional = sprint.containsDomainElement(classElement);

                        if(classOptional.isPresent()){
                            classOptional.get().setColorEnum(ColorEnum.MODIFIED);
                        } else {
                            classElement.setColorEnum(ColorEnum.REMOVED);
                        }
                    }

                    for (Method methodElement: s.getMethods()) {
                        DTORepository.get().fill(methodElement);

                        Optional<Method> methodOptional = sprint.containsDomainElement(methodElement);

                        if(methodOptional.isPresent()){
                            methodOptional.get().setColorEnum(ColorEnum.MODIFIED);
                        } else {
                            methodElement.setColorEnum(ColorEnum.REMOVED);
                        }
                    }

                    sprint.getStoryList().add(s);
                    s.setColorEnum(ColorEnum.REMOVED);
                }
        );

        Parser.parseSprints(Collections.singletonList(sprint), "/data/node.csv","/data/edge.csv");
    }

    private void visualiseAddStories() throws IOException {
        super.execute();

        Sprint sprint = this.initSprint();

        List<UserStory> stories = DTORepository.get().getStoriesIn(this.storyIds);

        stories.forEach(s -> {
            DTORepository.get().fill(s);

            for (Class classElement: s.getClasses()) {
                Optional<Class> classOptional = sprint.containsDomainElement(classElement);

                if(classOptional.isPresent()){
                    classOptional.get().setColorEnum(ColorEnum.MODIFIED);
                } else {
                    classElement.setColorEnum(ColorEnum.ADDED);
                }
            }

            for (Method methodElement:
                    s.getMethods()) {
                Optional<Method> methodOptional = sprint.containsDomainElement(methodElement);

                if(methodOptional.isPresent()){
                    methodOptional.get().setColorEnum(ColorEnum.MODIFIED);
                } else {
                    methodElement.setColorEnum(ColorEnum.ADDED);
                }
            }

            s.setColorEnum(ColorEnum.ADDED);
            sprint.getStoryList().add(s);
        });

        Parser.parseSprints(Collections.singletonList(sprint), "/data/node.csv","/data/edge.csv");
    }

    private Sprint initSprint(){
        Sprint res = DTORepository.get().getSprint(this.sprintName);

        res.setColorEnum(ColorEnum.MODIFIED);

        for (UserStory story : res.getStoryList()) {
            story.setColorEnum(ColorEnum.DEFAULT);
            story.getClasses().forEach(c -> c.setColorEnum(ColorEnum.DEFAULT));
            story.getMethods().forEach(m -> m.setColorEnum(ColorEnum.DEFAULT));
        }

        return res;
    }

    @Override
    public void load(List<String> args) {
        super.load(args);

        if(args.size() > 0){
            addMode = Boolean.valueOf(args.get(0));
            args.remove(0);
        }
    }

    @Override
    protected void check() throws IOException {
        if(DTORepository.get().getSprint(this.sprintName) == null) {
            throw new IOException("The sprint named " + this.sprintName + " wasn't found.");
        }
    }

    @Override
    protected int getNbOtherArgs() {
        return 1;
    }

    @Override
    public String describe() {
        return "Visualise the impact of a story on the selected sprint\n" +
                "       - name:String\n" +
                "       - addMode:Boolean\n" +
                "       - stories:List<Integer>";
    }
}