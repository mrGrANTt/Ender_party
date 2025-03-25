package org.ven.mrg.ender_party.custom;


import org.bukkit.Bukkit;
import org.ven.mrg.ender_party.Values;

import java.sql.SQLException;

public class BackupBlocks {
    public static boolean backup() {
        try {
            for (BlockInfo blockInfo : BlockDB.getAllBlocks())
                restoreBlockFromNBT(blockInfo);
        } catch (SQLException e) {
            Values.getPlg().getLogger().warning("Error in regeneration blocks... Try again!\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void restoreBlockFromNBT(BlockInfo blockInfo) {
        blockInfo.loc.getBlock().setBlockData(Bukkit.createBlockData(blockInfo.json));
    }
}
