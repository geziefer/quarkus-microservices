package de.syrocon.cajee;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import io.quarkus.logging.Log;

@GraphQLApi 
public class HeroesAPI {
    @Inject
    HeroRepository repository;

    @Query("randomHero")
    public Hero random() {
        Hero hero = repository.getRandomHero();
        Log.info("Produced random hero: " + hero.name);
        return hero;
    }  
}

