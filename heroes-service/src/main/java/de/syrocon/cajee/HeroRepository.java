package de.syrocon.cajee;

import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class HeroRepository {

    private final List<Hero> heroes;
    private final Random random = new Random();

    public List<Hero> getAllHeroes() {
        return heroes;
    }

    public Hero getRandomHero() {
        int index = random.nextInt(heroes.size());
        return heroes.get(index);
    }

    public HeroRepository() {
        heroes = List.of(new Hero("Chewbacca", "", "https://www.superherodb.com/pictures2/portraits/10/050/10466.jpg",
                        "Agility, Longevity, Marksmanship, Natural Weapons, Stealth, Super Strength, Weapons Master", 5));}

}
