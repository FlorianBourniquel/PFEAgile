package fr.unice.polytech.web;

import javax.ws.rs.core.Response;
import java.util.*;

public class CmdProcessor {

    private Map<String, Class<? extends WebCommand>> commands = new HashMap<>();
    private static CmdProcessor instance;

    @SafeVarargs
    public final void register(Class<? extends WebCommand>... commands) {
        for(Class<? extends WebCommand> c: commands) {
            registerCommand(c);
        }
    }

    private void registerCommand(Class<? extends WebCommand> command) {
        try {
            String identifier = command.newInstance().identifier();
            if (identifier.contains(" "))
                throw new IllegalArgumentException("Identifier cannot contain whitespace");
            commands.put(identifier, command);
        } catch(InstantiationException|IllegalAccessException|IllegalArgumentException e) {
            System.err.println("Unable to register command " + command.toString());
            e.printStackTrace();
        }
    }


    Response executeCmd(String name, List<String> args) throws CmdException, IllegalAccessException, InstantiationException {
        String identifier = commands.keySet().stream()
                .filter(x -> x.equals(name))
                .findFirst().orElseThrow(() -> new CmdException("Commande inconnue"));

        WebCommand x = commands.get(identifier).newInstance();
        try {
            x.load(args);
        }catch (Exception e){
            throw new CmdException(e.getMessage());
        }
        return x.execResponse();
    }

    public static CmdProcessor get() {
        if(instance == null){
            instance = new CmdProcessor();
        }
        return instance;
    }
}
