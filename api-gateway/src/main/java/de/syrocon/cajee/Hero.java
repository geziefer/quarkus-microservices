package de.syrocon.cajee;

public class Hero {
    public String name;
    public String picture;
    public int level;

    public FightServiceOuterClass.Hero toGrpc() {
        return FightServiceOuterClass.Hero.newBuilder()
                .setLevel(level)
                .setName(name)
                .setImage(picture)
                .build();
    }

    public static Hero fromGrpc(FightServiceOuterClass.Hero h) {
        Hero hero = new Hero();
        hero.name = h.getName();
        hero.level = h.getLevel();
        hero.picture = h.getImage();
        return hero;
    }
}
