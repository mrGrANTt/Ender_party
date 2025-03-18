package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
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
        eman.setMetadata("EnderClown", new FixedMetadataValue(Values.getPlg(), true));
        eman.setAI(false);
        //TODO: add gravity

        while (!eman.teleportRandomly()) {}

        eman.getEquipment().setHelmet(getClownHead());
        eman.getEquipment().setDropChance(EquipmentSlot.HEAD, 1f);
        return eman;
    }

    public static boolean isClown(@NotNull Entity entity) {
        return entity.hasMetadata("EnderClown");
    }

    private boolean protectedBlock(Block block) {
        return block.getType().equals(Material.BEDROCK)
                || block.getType().equals(Material.COMMAND_BLOCK)
                || block.getType().equals(Material.CHAIN_COMMAND_BLOCK)
                || block.getType().equals(Material.REPEATING_COMMAND_BLOCK)
                || block.getType().equals(Material.BARRIER)
                || block.getType().equals(Material.STRUCTURE_BLOCK)
                || block.getType().equals(Material.STRUCTURE_VOID);
    }

    @Override
    public void run() {
        switch (phase) {
            case GET_BLOCK:
                if (!eman.isDead()) {
                    Block blc = eman.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (blc.getType().equals(Material.AIR)) {
                        while (!eman.teleportRandomly()) {}
                    } else {
                        if (!protectedBlock(blc)) {
                            Values.logWithType(0, "[EnderController] Stealing block in %d %d %d!"
                                    .formatted(
                                            blc.getLocation().getBlockX(),
                                            blc.getLocation().getBlockY(),
                                            blc.getLocation().getBlockZ()));

                            eman.setCarriedBlock(blc.getBlockData());
                            blc.setType(Material.AIR);
                        } else {
                            Values.logWithType(0, "[EnderController] Don't stealing block in %d %d %d!".formatted(
                                    blc.getLocation().getBlockX(),
                                    blc.getLocation().getBlockY(),
                                    blc.getLocation().getBlockZ()));
                        }

                        phase = EnderPhase.RUN_OUT;
                    }
                    new EnderController(phase, eman).runTaskLater(200);
                }
                break;

            case RUN_OUT:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man running out!");
                    while (!eman.teleportRandomly()) {}
                    phase = EnderPhase.HIDE;
                    new EnderController(phase, eman).runTaskLater(200);
                }
                break;

            case HIDE:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man hide!");
                    //TODO: save in db
                    Location loc = eman.getLocation();
                    loc.setY(-400);
                    eman.teleport(loc);
                }
                break;
        }
    }
}
