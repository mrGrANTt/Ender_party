package org.ven.mrg.ender_party.custom;

import org.bukkit.entity.Player;
import org.ven.mrg.ender_party.Values;


public class EnderRunnable extends MrgRunnable {
    private final Player plr;

    private EnderRunnable(Player plr) {
        super();
        this.plr = plr;
    }

    public static void init(Player plr) {
        new EnderRunnable(plr).runTaskLater(Values.getRandomSpawnTime());
    }

    @Override
    public void run() {
        try {
            if (!plr.isOnline()) {
                Values.logWithType(0, "Task " + getTaskId() + " will be stopped! " + plr.getName() + " was offline");
                this.cancel();
                return;
            }
            Values.logWithType(0, "Task " + getTaskId() + " start process for " + plr.getName() + ")");
            new EnderController(plr.getLocation(), EnderPhase.SPAWN).runTask();

            runTaskLater(Values.getRandomSpawnTime());
        } catch (IllegalStateException e) {
            Values.logWithType(0, "Task " + getTaskId() + " by " + plr.getName() + " was not scheduled yet!");
        }
    }
}