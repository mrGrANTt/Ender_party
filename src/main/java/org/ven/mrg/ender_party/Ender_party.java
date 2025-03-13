package org.ven.mrg.ender_party;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.ven.mrg.ender_party.commands.CommandSystem;
import org.ven.mrg.ender_party.commands.CommandTab;
import org.ven.mrg.ender_party.events.Events;

public final class Ender_party extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Values.setPlg(this);
        Values.logWithType(0, "Values loaded!");


        PluginCommand cmd = getCommand("joke");
        if(cmd != null) {
            cmd.setExecutor(new CommandSystem());
            cmd.setTabCompleter(new CommandTab());
            Values.logWithType(0, "Commands loaded!");
        }

        getServer().getPluginManager().registerEvents(new Events(), this);
        Values.logWithType(0, "Events loaded!");
        Values.logWithType(1, "Plugin loaded!");
    }
}
