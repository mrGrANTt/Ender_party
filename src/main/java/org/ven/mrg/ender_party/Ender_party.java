package org.ven.mrg.ender_party;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.ven.mrg.ender_party.commands.CommandSystem;
import org.ven.mrg.ender_party.commands.CommandTab;
import org.ven.mrg.ender_party.custom.BlockDB;
import org.ven.mrg.ender_party.events.Events;

import java.sql.SQLException;

public final class Ender_party extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Values.setPlg(this);
        Values.logWithType(1, "Values loaded!");

        PluginCommand cmd = getCommand("joke");
        if(cmd != null) {
            cmd.setExecutor(new CommandSystem());
            cmd.setTabCompleter(new CommandTab());
            Values.logWithType(1, "Commands loaded!");
        }

        try {
            BlockDB.LodeAllProtectedBlocks();
        } catch (SQLException e) {
            Values.logWithType(1, "Protected blocks not loaded!");
            e.printStackTrace();
        }

        getServer().getPluginManager().registerEvents(new Events(), this);
        Values.logWithType(1, "Events loaded!");
        Values.logWithType(2, "Plugin loaded!");
    }
}
