package com.qhuy.coordLeak.utils;

import com.qhuy.coordLeak.CoordLeak;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public class message {

    public static Component parse(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }
    public static String chat(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
    public static String get(String placeholder) {
        return CoordLeak.getInstance().getMessage().getString(placeholder);
    }
    public static Component format(String path, Map<String, String> placeholders) {
        String msg = CoordLeak.getInstance().getMessage().getString(path, "Invalid message");
        for(Map.Entry<String, String> entry : placeholders.entrySet()) {
            msg = msg.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return parse(msg);
    }
}
