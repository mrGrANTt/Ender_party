package org.ven.mrg.ender_party.custom;

import org.bukkit.entity.Enderman;

public class EnderNoAIGravity extends MrgRunnable {
    private Enderman eman;

    public EnderNoAIGravity(Enderman eman) {
        this.eman = eman;
    }

    @Override
    public void run() {
        if (eman == null || eman.isDead()) this.cancel();
        //TODO: move down
    }
}
