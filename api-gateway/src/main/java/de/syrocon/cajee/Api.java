package de.syrocon.cajee;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

@Path("/api")
public class Api {
    @Inject
    @RestClient
    VillainClient villains;

    @Inject 
    HeroesClient heroes;

    @GrpcClient("fight-service")
    FightService fight;

    @GET
    public Uni<Fight> fight() {
        Uni<Villain> villain = villains.getVillain();
        Uni<Hero> hero = heroes.getRandomHero();

        return Uni.combine().all().unis(hero, villain).asTuple()
                .chain(tuple -> {
                    Hero h = tuple.getItem1();
                    Villain v = tuple.getItem2();

                    return invokeFightService(fight, h, v);
                });
    }

    private Uni<Fight> invokeFightService(FightService fs, Hero hero, Villain villain) {
        FightServiceOuterClass.Fighters fighters = FightServiceOuterClass.Fighters.newBuilder()
                .setHero(hero.toGrpc())
                .setVillain(villain.toGrpc())
                .build();

        return fs.fight(fighters)
                .onItem().transform(result -> {
                        String winner = result.getWinner();
                        return new Fight(hero, villain, winner);
        });
    }
}
