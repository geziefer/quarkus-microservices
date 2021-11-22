package de.syrocon.cajee;

import javax.inject.Inject;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

@GraphQLApi 
public class HeroesAPI {
    @Inject
    HeroRepository repository;

    @Query("randomHero")
    public Hero random() {
        return repository.getRandomHero();
    }  
}

