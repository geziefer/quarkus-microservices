package de.syrocon.cajee;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;

@Path("/api")
public class Api {
    @Inject
    @RestClient
    VillainClient villains;

    @Inject 
    HeroesClient heroes;

    @GrpcClient("fight-service")
    FightService fight;

    @Channel("fights")
    MutinyEmitter<Fight> emitter;

    @GET
    public Uni<Fight> fight() {
        Uni<Villain> villain = villains.getVillain();
        Uni<Hero> hero = heroes.getRandomHero();

        return Uni.combine().all().unis(hero, villain).asTuple()
                .chain(tuple -> {
                    Hero h = tuple.getItem1();
                    Villain v = tuple.getItem2();

                    return invokeFightService(fight, h, v);
                })
                .call(fight -> emitter.send(fight));
    }

    @Retry(maxRetries = 5)
    @NonBlocking
    private Uni<Fight> invokeFightService(FightService fs, Hero hero, Villain villain) {
        FightServiceOuterClass.Fighters fighters = FightServiceOuterClass.Fighters.newBuilder()
                .setHero(hero.toGrpc())
                .setVillain(villain.toGrpc())
                .build();

        return fs.fight(fighters)
                .onItem().transform(result -> {
                        String winner = result.getWinner();
                        Log.info("Fight " + hero.name + " vs. " + villain.name + ": " + winner + " wins");
                        return new Fight(hero, villain, winner);
        });
    }
}
