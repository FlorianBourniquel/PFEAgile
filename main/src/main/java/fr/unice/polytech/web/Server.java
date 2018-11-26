package fr.unice.polytech.web;

import fr.unice.polytech.cli.commands.Bye;
import fr.unice.polytech.cli.commands.CreateSprint;
import fr.unice.polytech.cli.commands.ListBacklog;
import fr.unice.polytech.cli.commands.initbacklog.InitBacklog;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Server {

    private static final URI BASE_URI = URI.create("http://localhost:8080/");
    private static final String ROOT_PATH = "main";

    public static void main(String[] args) {
        CmdProcessor.get().register(Bye.class, ListBacklog.class, InitBacklog.class, CreateSprint.class);
        try {
            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, create(), false);
            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownNow));
            server.start();
            System.out.println(String.format("Application started.%n Try out %s%s%n Stop the application using CTRL+C", BASE_URI, ROOT_PATH));
            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static ResourceConfig create() {
        return new ResourceConfig(MainController.class);
    }
}