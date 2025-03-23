package org.ven.mrg.ender_party.events;


import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.ven.mrg.ender_party.Values;
import org.ven.mrg.ender_party.custom.EnderController;
import org.ven.mrg.ender_party.custom.EnderRunnable;

public class Events implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        if(Values.isEnabled()) {
            Values.logWithType(0, "[Events] Start task for " + ev.getPlayer().getName());
            EnderRunnable.init(ev.getPlayer());
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent ev) {
        if (EnderController.isClown(ev.getEntity())) {
            Values.logWithType(0, "[Events] Canceled EntityChangeBlockEvent by clown!");
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent ev) {
        if(!Values.hasClownDead() && ev.getEntity().getType().equals(EntityType.ENDERMAN) && ev.getEntity().hasMetadata("EnderClown")) {
            ev.setCancelled(true);
        }
    }
}
