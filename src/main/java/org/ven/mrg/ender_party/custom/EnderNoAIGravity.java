package org.ven.mrg.ender_party.custom;

import org.bukkit.entity.Enderman;
import org.ven.mrg.ender_party.Values;

public class EnderNoAIGravity extends MrgRunnable {
    private Enderman eman;
    private int log_counter;

    public EnderNoAIGravity(Enderman eman) {
        this.eman = eman;
        log_counter = 0;
    }

    @Override
    public void run() {
        if (eman == null || eman.isDead()) this.cancel();

        if (log_counter == 40) {
            log_counter = 0;
            Values.logWithType(0, "[EnderNoAIGravity] Check enderman pos");
        }
        Values.logWithType(0, "Is on ground: " + eman.isOnGround());
        if (!eman.isOnGround()) {//TODO: other check
            if (log_counter == 0)
                Values.logWithType(0, "[EnderNoAIGravity] Enderman isn't on block" + eman.getLocation());
            eman.teleport(eman.getLocation().add(0, -0.1d, 0));
            if (log_counter == 0)
                Values.logWithType(0, "[EnderNoAIGravity] Enderman loc setted to " + eman.getLocation());
        }
        log_counter++;
    }
}
