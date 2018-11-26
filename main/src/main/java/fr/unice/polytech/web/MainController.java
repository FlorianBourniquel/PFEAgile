
package fr.unice.polytech.web;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("main")
public class MainController {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response main(CmdRequest request) {
        try {
            return  CmdProcessor.get().executeCmd(request.getCmd(),request.getArgs());
        } catch (IllegalAccessException | InstantiationException e) {
            return Response.serverError().entity("Erreur interne lors de l'execution de la commande").build();
        } catch (CmdException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

}