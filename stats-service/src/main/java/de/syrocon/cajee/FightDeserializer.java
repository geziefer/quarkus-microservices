package de.syrocon.cajee;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class FightDeserializer extends ObjectMapperDeserializer<Fight> {
    public FightDeserializer() {
        super(Fight.class);
    }
}
