package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockInfo {
    public Location loc;
    public String json;

    public BlockInfo(Location loc, String json) {
        this.json = json;
        this.loc = loc;
    }

    public BlockInfo(Block block) {
        loc = block.getLocation();
        json = "{\"type\":\"test_json\"}"; //TODO: nbt
    }

    @Override
    public String toString() {
        return "BlockInfo@{" + loc.toString() + ":" + json + "}";
    }
}
