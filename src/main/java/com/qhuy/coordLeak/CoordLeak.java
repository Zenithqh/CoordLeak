package com.qhuy.coordLeak;

import com.qhuy.coordLeak.commands.buyUsage;
import com.qhuy.coordLeak.commands.coordCommand;
import com.qhuy.coordLeak.commands.reload;
import com.qhuy.coordLeak.commands.setUsage;
import net.milkbowl.vault.economy.Economy;
import com.qhuy.coordLeak.utils.DatabaseManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public final class CoordLeak extends JavaPlugin {
    private static CoordLeak instance;
    private BukkitAudiences adventure;
    private Economy econ;
    private DatabaseManager databaseManager;
    private FileConfiguration messages;
    private File file;


    public static CoordLeak getInstance() { return instance; }
    public BukkitAudiences adventure() { return this.adventure; }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        info("Enabling");
        instance = this;

        file = new File(getDataFolder(), "messages.yml");
        if(!file.exists()) {
            CoordLeak.getInstance().saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(file);

        this.adventure = BukkitAudiences.create(this);
        if(!setupEconomy()) {
            getLogger().warning("Could not setup Economy, disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        databaseManager = new DatabaseManager();
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                databaseManager.connect();
                getLogger().info("Database connected");
            } catch (SQLException e) {
                getLogger().warning("(\"Error while connecting to the database, disabling plugin...\");");
                Bukkit.getScheduler().runTask(this, () -> {
                    Bukkit.getPluginManager().disablePlugin(this);
                });
            }
        });
        Bukkit.getPluginCommand("buyusage").setExecutor(new buyUsage(this, databaseManager));
        Bukkit.getPluginCommand("coord").setExecutor(new coordCommand(this, databaseManager));
        Bukkit.getPluginCommand("creload").setExecutor(new reload(this));
        Bukkit.getPluginCommand("setusage").setExecutor(new setUsage(this, databaseManager));
    }

    @Override
    public void onDisable() {
        info("Disabling");
        if(databaseManager != null) {
            Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
                databaseManager.disconnect(this);
                getLogger().info("Database disconnected");
            });
        }
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
        saveConfig();
    }
    public FileConfiguration getMessage() {
        return messages;
    }

    private void info(String msg) {
        StringBuilder text = new StringBuilder("\n\n");
        text.append("&8[]===========[").append(msg).append(" &cCoordLeak&8]===========[]\n");
        text.append("&8|\n");
        text.append("&8| &cInformation:\n");
        text.append("&8|\n");
        text.append("&8|   &9Name: &bCoordLeak\n");
        text.append("&8|   &9Author: ").append(getDescription().getAuthors()).append("\n");
        text.append("&8|\n");
        text.append("&8| &9Contact:\n");
        text.append("&8|   &9Email: &bzenythqh@gmail.com\n");
        text.append("&8|   &9Discord: &b@qhuy120420\n");
        text.append("&8|   &9Modrinth: &bZenythqh\n");
        text.append("&8|\n");
        text.append("&8[]=========================================[]\n");

        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text.toString()));
    }

    private boolean setupEconomy() {
        if(getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }
    public Economy getEconomy() {
        return econ;
    }
    public void reload() {
        messages = YamlConfiguration.loadConfiguration(file);
    }
}
