package org.ven.mrg.ender_party;

import java.util.Random;

public class Values {
    private static Ender_party plg;
    private static final Random rand = new Random();

    public static void reload() {}

    public static void setPlg(Ender_party value) { plg = value; }
    public static Ender_party getPlg() { return plg; }

    public static void logWithType(int type, String msg) {
        if(type <= plg.getConfig().getInt("log-type", 1))
            plg.getLogger().info(msg);
    }








    public static boolean isEnabled() { return plg.getConfig().getBoolean("enabled"); }

    public static int getRandomSpawnTime() {
        return rand.nextInt(
            plg.getConfig().getInt("spawn-clown-timer.min"),
            plg.getConfig().getInt("spawn-clown-timer.max") + 1
        ); //* 20;
    }

    public static int randomSpawnRadius() {
        return rand.nextInt(1, plg.getConfig().getInt("spawn-radius"));
    }

    public static boolean hasClownAI() { return plg.getConfig().getBoolean("clown-ai"); }
    public static boolean hasClownDead() { return plg.getConfig().getBoolean("clown-dead"); }
}
