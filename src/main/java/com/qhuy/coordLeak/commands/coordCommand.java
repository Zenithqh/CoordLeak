package com.qhuy.coordLeak.commands;

import com.qhuy.coordLeak.CoordLeak;
import com.qhuy.coordLeak.utils.DatabaseManager;
import com.qhuy.coordLeak.utils.message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class coordCommand implements CommandExecutor {
    private final CoordLeak plugin;
    public coordCommand(CoordLeak plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        if(!(sender instanceof Player player)) {
            return true;
        }
        if(args.length != 0) {
            return true;
        }
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        players.remove(sender);
        String prefix = plugin.getConfig().getString("prefix", "");
        if(players.isEmpty()) {
            player.sendMessage(message.parse(message.get("noOneIsOnline")));
            return true;
        }
        DatabaseManager.getUsageCountAsync(player.getUniqueId(), plugin, (count) -> {
            if (count > 0) {
                player.sendMessage(message.parse(prefix + " " + message.get("soPoor")));
                return;
            }

            Player target = players.get(ThreadLocalRandom.current().nextInt(players.size()));
            DatabaseManager.onUsageAsync(player.getUniqueId(), plugin);

            double x = target.getX();
            double z = target.getZ();
            String dimension = target.getWorld().getName();

            player.sendMessage(message.format(prefix + "randomSelect.message", Map.of()));
            player.sendMessage(message.format("randomSelect.target", Map.of("player", target.getName())));
            player.sendMessage(message.format("randomSelect.coord", Map.of(
                    "x", String.valueOf(x),
                    "z", String.valueOf(z)
            )));
            player.sendMessage(message.format("randomSelect.dimension", Map.of("dimension", dimension)));
            target.sendMessage(message.parse(message.get("leak.exposed")) );
        });

        return true;
    }
}
