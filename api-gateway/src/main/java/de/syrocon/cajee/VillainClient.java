package de.syrocon.cajee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;

@RegisterRestClient(configKey = "villain-service")
public interface VillainClient {
    @GET
    @Path("/")
    @NonBlocking
    @CircuitBreaker
    Uni<Villain> getVillain();
}
