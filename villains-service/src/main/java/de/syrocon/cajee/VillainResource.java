package de.syrocon.cajee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class VillainResource {

    @GET
    public Villain getRandomVillain() {
        return new Villain();
    }

}