package de.syrocon.cajee;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Broadcast;

@ApplicationScoped
public class FightProcessor {

    private final WinRatio ratio = new WinRatio();

    @Incoming("fights")
    @Outgoing("ratio")
    @Broadcast
    public WinRatio ratio(Fight f) {
        ratio.accumulate(f);
        Log.info("Total: " + ratio.getCount() + ", Heroes: " + ratio.getHero() + ", Villains: " + ratio.getVillain());
        return ratio;
    }
}
