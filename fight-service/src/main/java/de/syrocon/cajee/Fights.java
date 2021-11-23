package de.syrocon.cajee;

import java.time.Duration;
import java.util.Random;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

public class Fights {
    public static Uni<String> fight(FightServiceOuterClass.Hero hero, FightServiceOuterClass.Villain villain) {
        Random random = new Random();
        int actualHeroLevel = hero.getLevel() + random.nextInt(50);
        int actualVillainLevel = villain.getLevel() + random.nextInt(50);
        int duration = random.nextInt(500);

        if (actualHeroLevel > actualVillainLevel) {
            Log.info("Hero wins: " + hero.getName());
            return Uni.createFrom().item(hero.getName())
                    .onItem().delayIt().by(Duration.ofMillis(duration));
        } else if (actualHeroLevel < actualVillainLevel) {
            Log.info("Villain wins: " + villain.getName());
            return Uni.createFrom().item(villain.getName())
                    .onItem().delayIt().by(Duration.ofMillis(duration));
        } else {
            Log.info("A draw, fight again");
            return fight(hero, villain);
        }
    }
}
