package de.syrocon.cajee;

import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.core.OperationType;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class HeroesClient {
    @Inject
    @GraphQLClient("heroes-service")
    DynamicGraphQLClient qlClient;

    public Uni<Hero> getRandomHero() {
        
        Document randomHero = document(
                operation(OperationType.QUERY,
                        field("randomHero",
                            field("name"),
                            field("level"),
                            field("picture:image")
                        )));

        return qlClient.executeAsync(randomHero)
                .onItem().transform(response -> response.getObject(Hero.class, "randomHero"));
    }
}
