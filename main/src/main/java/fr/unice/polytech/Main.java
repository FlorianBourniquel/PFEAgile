package fr.unice.polytech;

import fr.unice.polytech.cli.commands.*;
import fr.unice.polytech.cli.commands.initbacklog.InitBacklog;
import fr.unice.polytech.cli.commands.whatif.WhatIfIAddStory;
import fr.unice.polytech.cli.commands.whatif.WhatIfIRemoveStory;
import fr.unice.polytech.cli.framework.Shell;
import fr.unice.polytech.environment.Environment;

public class Main  extends Shell<Environment>
{

     Main(){
        this.system = new Environment();
        this.invite  = "Agile";
        register(
                Bye.class,
                InitBacklog.class,
                CreateSprint.class,
                ListStories.class,
                VisualiseModel.class,
                VisualiseImpact.class,
                WhatIfIAddStory.class,
                WhatIfIRemoveStory.class,
                SortBacklogByValue.class
        );
    }


    public static void main( String[] args ) throws InterruptedException {
        //Thread.sleep(10000);
        Main main = new Main();
        main.run();
    }


}
