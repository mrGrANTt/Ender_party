package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.ven.mrg.ender_party.Values;


public class EnderController extends MrgRunnable {
    private final Location location;
    private EnderPhase phase;

    public EnderController(Location location, EnderPhase phase) {
        this.location = location;
        this.phase = phase;
    }

    @Override
    public void run() {
        switch (phase) {
            case SPAWN:
                Values.logWithType(0, "Spawning new e-man in " + location.toString() + "!");
                //TODO: some code

                phase = EnderPhase.GET_BLOCK;
                runTaskLater(100);
                break;
            case GET_BLOCK:
                Values.logWithType(0, "Stealing block in {Location of block}!");
                //TODO: some code

                phase = EnderPhase.RUN_OUT;
                runTaskLater(200);
                break;
            case RUN_OUT:
                Values.logWithType(0, "E-man running out!");
                //TODO: some code
                cancel();
                break;
        }
    }
}
