package fr.unice.polytech.web;

import javax.ws.rs.core.Response;
import java.util.List;

public interface WebCommand {
    String identifier();
    Response execResponse();
    void load(List<String> args) throws Exception;
}
