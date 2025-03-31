package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.ven.mrg.ender_party.Values;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EnderController extends MrgRunnable {
    private EnderPhase phase;
    private final Enderman eman;
    private BlockInfo block;





    public EnderController(EnderPhase phase, Enderman eman, BlockInfo block) {
        this.phase = phase;
        this.eman = eman;
        this.block = block;
    }

    public EnderController(Location loc, boolean teleport) {
        this(EnderPhase.GET_BLOCK, getClown(loc, teleport), null);
    }

    public EnderController(Location loc) {
        this(loc, true);
    }






    private static ItemStack getClownHead() {
        ItemStack item = new ItemStack(Material.CARVED_PUMPKIN);
        item.editMeta((e) -> e.setCustomModelData(10425));
        return item;
    }

    public static Enderman getClown(Location loc, boolean teleport) {

        if (teleport) {
            Location location = getLocation(loc);
            if (location != null) loc = location;
        }

        Values.logWithType(0, "[EnderController] Spawning new e-man in " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ() + "!");
        Enderman eman = (Enderman) loc.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
        eman.setMetadata("EnderClown", new FixedMetadataValue(Values.getPlg(), true));

        if (!Values.hasClownAI()) {
            eman.setAI(false);
            //new EnderNoAIGravity(eman).runTaskTimer(0,1);
        }

/*
        eman.getEquipment().setHelmet(getClownHead());
        eman.getEquipment().setDropChance(EquipmentSlot.HEAD, 1f);
*/
        return eman;
    }

    private static Location getLocation(Location loc) {
        class Loc {
            private final int x;
            private final int y;
            private final int z;

            public Loc(int x, int y, int z) {
                this.x = x;
                this.y = y;
                this.z = z;
            }

            public Location getLocation(World world) {
                return new Location(world, x, y, z);
            }
        }

        int blockX = loc.getBlockX();
        int blockY = loc.getBlockY();
        int blockZ = loc.getBlockZ();

        List<Loc> locs = new ArrayList<>();
        int r = Values.getSpawnRadius();
        for(int x = -r; x <= r; x++)
            for(int y = -r; y <= r; y++)
                for(int z = -r; z <= r; z++)
                    if (
                           isValidBlock(loc.getWorld().getBlockAt(blockX + x, blockY + y, blockZ + z))
                                   && isValidBlock(loc.getWorld().getBlockAt(blockX + x, blockY + y+1, blockZ + z))
                                   && isValidBlock(loc.getWorld().getBlockAt(blockX + x, blockY + y+2, blockZ + z))
                                   && !isValidBlock(loc.getWorld().getBlockAt(blockX + x, blockY + y-1, blockZ + z))
                    ) locs.add(new Loc(x,y,z));

        if(locs.isEmpty()) return null;
        return locs.get(Values.getRand().nextInt(0, locs.size())).getLocation(loc.getWorld()).add(blockX,blockY,blockZ);
    }

    private static boolean isValidBlock(Block block) {
        Values.logWithType(0, block.getType().toString());
        return block.getType().toString().contains("AIR") || block.getType().equals(Material.STRUCTURE_VOID);
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
                || block.getType().equals(Material.LIGHT)
                || block.getType().equals(Material.STRUCTURE_BLOCK)
                || block.getType().equals(Material.STRUCTURE_VOID)
                || block.getState() instanceof InventoryHolder
                || block.getType().equals(Material.BEEHIVE)
                || block.getType().equals(Material.BEE_NEST);
    }

    @Override
    public void run() {
        switch (phase) {
            case GET_BLOCK:
                if (!eman.isDead()) {
                    Block blc = eman.getLocation().getBlock().getRelative(BlockFace.DOWN);
                    if (blc.getType().equals(Material.AIR) || protectedBlock(blc)) {
                        teleport(10);
                    } else {
                        Values.logWithType(0, "[EnderController] Stealing block in %d %d %d!"
                                .formatted(
                                        blc.getLocation().getBlockX(),
                                        blc.getLocation().getBlockY(),
                                        blc.getLocation().getBlockZ()));

                        block = new BlockInfo(blc);
                        Values.logWithType(0, block.toString());
                        eman.setCarriedBlock(blc.getBlockData());

                        blc.setType(Material.LIGHT);
                        Levelled lg = (Levelled) blc.getBlockData();
                        lg.setLevel(0);
                        blc.setBlockData(lg, true);

                        blc.setMetadata("protected-block", new FixedMetadataValue(Values.getPlg(), true));
                        try {
                            BlockDB.addProtectedBlock(blc.getLocation());
                            Values.logWithType(0, "[EnderController] Protected block added to db!");
                        } catch (SQLException e) {
                            Values.logWithType(0, "[EnderController] Added protected block to db fail!");
                            e.printStackTrace();
                        }
                        phase = EnderPhase.RUN_OUT;
                    }
                    new EnderController(phase, eman, block).runTaskLater(200);
                }
                break;

            case RUN_OUT:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man running out!");
                    teleport(10);
                    phase = EnderPhase.HIDE;
                    new EnderController(phase, eman, block).runTaskLater(200);
                }
                break;

            case HIDE:
                if (!eman.isDead()) {
                    Values.logWithType(0, "[EnderController] E-man hide!");
                    try {
                        if(eman.getCarriedBlock() != null) {
                            Values.logWithType(0, block.toString());
                            BlockDB.addBlock(block);
                        }
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
