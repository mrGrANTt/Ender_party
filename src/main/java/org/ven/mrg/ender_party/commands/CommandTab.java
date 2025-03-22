package org.ven.mrg.ender_party.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CommandTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("vensoly7.joke")) {
            if (args.length <= 1) return List.of("help","reload","backup","disable","spawn_clown");
            return switch (args[0]) {
                case "spawn_clown" -> List.of("~ ~ ~", "~ ~", "~");
                case "help", "reload", "backup", "disable" -> List.of();
                default -> List.of("help","reload","backup","disable","spawn_clown");
            };
        } else {
            sender.sendMessage(Component.text("You have not permission!").color(TextColor.color(255,10,10)));
            return List.of();
        }
    }
}
