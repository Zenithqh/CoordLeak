package com.qhuy.coordLeak.commands;

import com.qhuy.coordLeak.CoordLeak;
import com.qhuy.coordLeak.utils.DatabaseManager;
import com.qhuy.coordLeak.utils.message;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class buyUsage implements CommandExecutor {
    private final CoordLeak plugin;

    public buyUsage(CoordLeak plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        String prefix = plugin.getConfig().getString("prefix", "");
        double price = plugin.getConfig().getDouble("price", 500);
        if(!(sender instanceof Player player)) {
            sender.sendMessage("shut the fuck up, only player can use this command");
            return true;
        }
        if(args.length != 0) {
            player.sendMessage(message.parse(prefix + " " + message.get("invalidArgument")));
            return true;
        }
        UUID targetUUID = player.getUniqueId();
        double balance = CoordLeak.getEconomy().getBalance(player);
        if(balance < price) {
            player.sendMessage(message.parse(prefix + " " + message.get("soPoor")));
            return true;
        }
        CoordLeak.getEconomy().withdrawPlayer(player, price);
        DatabaseManager.addUsageCountAsync(targetUUID, plugin);
        player.sendMessage(message.parse(prefix + " " + message.get("buySuccessfully")));

        return true;
    }
}
