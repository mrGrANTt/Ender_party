package org.ven.mrg.ender_party.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.ven.mrg.ender_party.Values;
import org.ven.mrg.ender_party.custom.BackupBlocks;
import org.ven.mrg.ender_party.custom.EnderController;


public class CommandSystem implements CommandExecutor {
    private static final Component help = Component.text(">> Commands:").appendNewline()
            .append(Component.text("help").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/joke help")))
            .append(Component.text(" - print this list"))
            .appendNewline()
            .append(Component.text("reload").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/joke reload")))
            .append(Component.text(" - reload plugin configuration"))
            .appendNewline()
            .append(Component.text("backup").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/joke backup")))
            .append(Component.text(" - regenerate stolen blocks"))
            .appendNewline()
            .append(Component.text("disable").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/joke disable")))
            .append(Component.text(" - disable clown spawning"))
            .appendNewline()
            .append(Component.text("spawn_clown").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/joke spawn_clown ~ ~ ~")))
            .append(Component.text(" <x,y,z> - spawn clown in x/y/z"))
            .appendNewline();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("vensoly7.joke")) {
            if (args.length < 1) return false;
            switch (args[0]) {
                case "help":
                    sender.sendMessage(help);
                    return true;
                case "reload":
                    Values.reload();
                    sender.sendMessage(Component.text("Configuration reloaded!").color(Values.getPlgColor()));
                    return true;
                case "backup":
                    if(BackupBlocks.backup()) {
                        sender.sendMessage(Component.text("Blocks are regenerated!").color(Values.getPlgColor()));
                    } else {
                        sender.sendMessage(Component.text("Blocks are NOT regenerated!").color(Values.getPlgColor()));
                    }
                    return true;
                case "disable":
                    Values.getPlg().getConfig().set("enabled", false);
                    Values.getPlg().saveConfig();
                    sender.sendMessage(Component.text("Mob spawning disabled!").color(Values.getPlgColor())
                            .appendNewline()
                            .append(Component.text("To enable again - change 'enabled' in config and restart server")));
                    return true;
                case "spawn_clown":
                    try {
                        boolean isPlayer = sender instanceof Player;

                        double x;
                        if (args[1].equals("~"))
                            if (isPlayer) x = ((Player)sender).getLocation().getX();
                            else throw new RuntimeException("is not player");
                        else x = Double.parseDouble(args[1]);

                        double y;
                        if (args[2].equals("~"))
                            if (isPlayer) y = ((Player)sender).getLocation().getY();
                            else throw new RuntimeException("is not player");
                        else y = Double.parseDouble(args[2]);

                        double z;
                        if (args[3].equals("~"))
                            if (isPlayer) z = ((Player)sender).getLocation().getZ();
                            else throw new RuntimeException("is not player");
                        else z = Double.parseDouble(args[3]);



                        new EnderController(new Location(Values.getPlg().getServer().getWorld(args.length >= 5 ? args[4] : "world"), x,y,z), false).runTaskLater(300);
                    } catch (Exception ex) {
                        if (ex.getMessage().equals("is not player"))
                            sender.sendMessage(Component.text("Only player can use ~").color(Values.getPlgColor()));
                        else
                            sender.sendMessage(Component.text("Invalid arguments ").color(Values.getPlgColor())
                                    .append(Component.text("Check /joke help")));
                    }
                    return true;
                default: return false;
            }
        } else {
            sender.sendMessage(Component.text("You have not permission!").color(TextColor.color(255,10,10)));
            return true;
        }
    }
}
