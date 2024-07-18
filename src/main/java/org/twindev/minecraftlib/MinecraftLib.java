package org.twindev.minecraftlib;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.logger.MinecraftLog;
import org.twindev.minecraftlib.logger.MinecraftLogLevels;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.util.HashMap;
import java.util.Map;

public final class MinecraftLib {
    private static MinecraftUtility utility;
    private static Economy econ = null;
    private static Map<String, YamlConfig> configs;

    public static void onLoad(@NotNull final MinecraftUtility plugin) {
        MinecraftLib.utility = plugin;
        configs = new HashMap<>();

        if (!setupEconomy()) {
            MinecraftLog.log(MinecraftLogLevels.ERROR, "Vault not found, disabling plugin");
            Bukkit.getPluginManager().disablePlugin(getPlugin());
            return;
        }
    }

    public static MinecraftUtility getUtility() {
        return utility;
    }

    public static JavaPlugin getPlugin() {
        return (JavaPlugin) utility;
    }

//    public static MinecraftAPI getLibrary() {
//        return (MinecraftAP) getPlugin();
//    }


    public static Economy getEconomy() {
        return econ;
    }

    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Map<String, YamlConfig> getMenuConfigs() {
        return configs;
    }
}
