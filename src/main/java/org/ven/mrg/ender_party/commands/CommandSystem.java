package org.ven.mrg.ender_party.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.ven.mrg.ender_party.Values;



public class CommandSystem implements CommandExecutor {
    private static Component help = Component.text("Commands:")
            .append(Component.text("help").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joke help")))
            .append(Component.text())
            .appendNewline()
            .append(Component.text("reload").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joke reload")))
            .append(Component.text())
            .appendNewline()
            .append(Component.text("disable").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joke disable")))
            .append(Component.text())
            .appendNewline()
            .append(Component.text("spawn_clown").color(Values.getPlgColor())
                    .decorate(TextDecoration.UNDERLINED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "joke spawn_clown ~ ~ ~")))
            .append(Component.text())
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
                    return true;
                case "disable":
                    Values.getPlg().getConfig().set("enabled", false);
                    Values.getPlg().saveConfig();
                    return true;
                case "spawn_clown": return true;
                default: return false;
            }
        } else {
            sender.sendMessage(Component.text("You have not permission!").color(TextColor.color(255,10,10)));
            return true;
        }
    }
}
