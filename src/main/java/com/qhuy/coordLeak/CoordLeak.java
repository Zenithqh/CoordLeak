package com.qhuy.coordLeak;

import com.qhuy.coordLeak.commands.buyUsage;
import com.qhuy.coordLeak.commands.coordCommand;
import com.qhuy.coordLeak.commands.reload;
import net.milkbowl.vault.economy.Economy;
import com.qhuy.coordLeak.utils.DatabaseManager;
import com.qhuy.coordLeak.utils.message;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CoordLeak extends JavaPlugin {
    private static CoordLeak instance;
    private BukkitAudiences adventure;
    private static Economy econ;
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
        try {
            databaseManager.connect();
            getLogger().info("Database connected");
        } catch (Exception e) {
            getLogger().warning("Error while connecting to the database, disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        Bukkit.getPluginCommand("buyusage").setExecutor(new buyUsage(this));
        Bukkit.getPluginCommand("coord").setExecutor(new coordCommand(this));
        Bukkit.getPluginCommand("creload").setExecutor(new reload(this));
    }

    @Override
    public void onDisable() {
        info("Disabling");
        if(databaseManager != null) {
            databaseManager.disconnect();
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
        text.append("&8[]=========================================[]\n");

        Bukkit.getConsoleSender().sendMessage(message.chat(text.toString()));
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
    public static Economy getEconomy() {
        return econ;
    }
    public void reload() {
        messages = YamlConfiguration.loadConfiguration(file);
    }
}
