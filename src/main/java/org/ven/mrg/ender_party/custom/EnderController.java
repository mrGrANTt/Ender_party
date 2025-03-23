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

import java.sql.SQLException;


public class EnderController extends MrgRunnable {
    private EnderPhase phase;
    private Enderman eman;

    public EnderController(Location loc, boolean teleport) {
        this(EnderPhase.GET_BLOCK, getClown(loc, teleport));
    }

    public EnderController(Location loc) {
        this(loc, true);
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

    public static Enderman getClown(Location loc, boolean teleport) {
        Values.logWithType(0, "[EnderController] Spawning new e-man in " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "!");
        Enderman eman = (Enderman) loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
        eman.setMetadata("EnderClown", new FixedMetadataValue(Values.getPlg(), true));

        if (!Values.hasClownAI()) {
            eman.setAI(false);
            new EnderNoAIGravity(eman).runTaskTimer(0, 1);
        }

        if (teleport) teleport(10, eman);

        eman.getEquipment().setHelmet(getClownHead());
        eman.getEquipment().setDropChance(EquipmentSlot.HEAD, 1f);
        return eman;
    }

    public static Enderman getClown(Location loc) {
        return getClown(loc, true);
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
                        teleport(10);
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
                    for(int i = 0; i < 10; i++) if (eman.teleportRandomly()) break;
                    phase = EnderPhase.HIDE;
                    new EnderController(phase, eman).runTaskLater(200);
                }
                break;

            case HIDE:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man hide!");
                    try {
                        BlockDB.addBlock(eman.getCarriedBlock());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    Location loc = eman.getLocation();
                    loc.setY(-400);
                    eman.teleport(loc);
                }
                break;
        }
    }

    private void teleport(int max) {
        for(int i = 0; i < max; i++)
            if (eman.teleportRandomly())
                break;
    }

    private static void teleport(int max, Enderman eman) {
        for(int i = 0; i < max; i++)
            if (eman.teleportRandomly())
                break;
    }
}
