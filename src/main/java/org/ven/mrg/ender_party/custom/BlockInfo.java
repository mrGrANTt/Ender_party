package org.ven.mrg.ender_party.custom;

import de.tr7zw.nbtapi.NBTBlock;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.ven.mrg.ender_party.Values;

public class BlockInfo {
    public Location loc;
    public String json;

    public BlockInfo(Location loc, String json) {
        this.json = json;
        this.loc = loc;
    }

    public BlockInfo(Block block) {
        loc = block.getLocation();
        json = block.getBlockData().getAsString();
    }

    @Override
    public String toString() {
        return "BlockInfo@{" + loc.toString() + ":" + json + "}";
    }
}
