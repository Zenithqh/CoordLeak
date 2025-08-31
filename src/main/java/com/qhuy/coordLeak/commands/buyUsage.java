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

import java.util.Map;
import java.util.UUID;

public class buyUsage implements CommandExecutor {
    private final CoordLeak plugin;

    public buyUsage(CoordLeak plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        String prefix = plugin.getConfig().getString("prefix", "");
        double price = plugin.getConfig().getDouble("price", 500);
        if(!(sender.isOp()) || sender instanceof BlockCommandSender) {
            sender.sendMessage(message.parse(prefix + " " + message.get("permission")));
            return true;
        }
        if(args.length != 1) {
            sender.sendMessage(message.parse(prefix + " " + message.get("invalidArgument")));
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        if(target == null) {
            sender.sendMessage(message.parse(prefix + " " + message.get("invalidPlayer")));
            return true;
        }
        UUID targetUUID = target.getUniqueId();
        double balance = CoordLeak.getEconomy().getBalance(target);
        if(balance < price) {
            target.sendMessage(message.parse(prefix + " " + message.get("notEnoughBalance")));
            return true;
        }
        DatabaseManager.addUsageCount(targetUUID);
        CoordLeak.getEconomy().withdrawPlayer(target, price);
        target.sendMessage(message.get("buySuccessfully"));

        return true;
    }
}
