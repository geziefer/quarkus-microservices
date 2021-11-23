package de.syrocon.cajee;

import io.quarkus.logging.Log;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class VillainResource {

    @GET
    public Villain getRandomVillain() {
        Villain villain = Villain.getRandomVillain();
        Log.info("Produced random villain: " + villain.name);
        return villain;
    }

}