package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.ven.mrg.ender_party.Values;


public class EnderController extends MrgRunnable {
    private EnderPhase phase;
    private Enderman eman;

    public EnderController(Location loc) {
        this.phase = EnderPhase.GET_BLOCK;
        this.eman = getClown(loc);
    }

    public EnderController(EnderPhase phase, Enderman eman) {
        this.phase = phase;
        this.eman = eman;
    }

    private static ItemStack getClownHead() {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        item.editMeta((e) -> e.setCustomModelData(10425));
        return item;
    }

    public static Enderman getClown(Location loc) {
        Values.logWithType(0, "[EnderController] Spawning new e-man in " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "!");
        Enderman eman = (Enderman) loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);

        eman.teleportRandomly();//TODO: edit to normal tp
        eman.setCanPickupItems(false);//TODO: not this

        eman.getEquipment().setHelmet(getClownHead());
        eman.getEquipment().setDropChance(EquipmentSlot.HEAD, 1f);
        return eman;
    }

    @Override
    public void run() {
        switch (phase) {
            case GET_BLOCK:
                if (!eman.isDead()) {
                    Block blc = eman.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    Values.logWithType(0, "[EnderController] Stealing block in %d %d %d!"
                            .formatted(
                                    blc.getLocation().getBlockX(),
                                    blc.getLocation().getBlockY(),
                                    blc.getLocation().getBlockZ()));
                    eman.setCarriedBlock(blc.getBlockData()); //TODO: save item with block nbt
                    blc.setType(Material.AIR);// TODO mb protect bedrok

                    phase = EnderPhase.RUN_OUT;
                    new EnderController(phase, eman).runTaskLater(200);
                }
                break;

            case RUN_OUT:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man running out!");
                    eman.teleportRandomly();
                    phase = EnderPhase.HIDE;
                    new EnderController(phase, eman).runTaskLater(200);
                }
                break;

            case HIDE:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man hide!");
                    //TODO: save in db
                    Location loc = eman.getLocation();
                    loc.setY(-66);
                    eman.teleport(loc);
                }
                break;
        }
    }
}
