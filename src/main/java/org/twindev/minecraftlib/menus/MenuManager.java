package org.twindev.minecraftlib.menus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.utils.ResourceUtil;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MenuManager {

    public MenuManager() {
    }

    public void loadMenus(String folder) throws IOException, URISyntaxException {
        List<String> fileNames = ResourceUtil.listResourceFiles(folder);
        for (String s : fileNames) {
            if (!s.endsWith(".yml")) return;
            String name = s.substring(0, s.length() - 4);
            new YamlConfig(folder + File.separator + name);
            MinecraftLib.getMenuConfigs().put(name, new YamlConfig(folder + File.separator + name));
        }
    }

    public void createMenus() {
        List<MenuItem> items = new ArrayList<>();

        for (Map.Entry<String, YamlConfig> entry : MinecraftLib.getMenuConfigs().entrySet()) {
            String key = entry.getKey();
            YamlConfig value = entry.getValue();

            ConfigurationSection section = value.options().getConfigurationSection("items");
            for (String itemKey : section.getKeys(false)) {
                MenuItem item = new MenuItem(value);
                items.add(item);
            }

            Menu menu = new Menu(value.options().getString("title"),
                    value.options().getStringList("pattern"),
                    items);

            for (int row = 0; row < menu.getRows(); row++) {
                String[] rowPattern = menu.getPattern().get(row).split(" ");
                for (int col = 0; col < rowPattern.length; col++) {
                    String itemKey = rowPattern[col];
                    if (section.contains(itemKey)) {
                        MenuItem item = new MenuItem(value);
                        menu.setItem(item, row * 9 + col);
                    }
                }
            }

        }

    }


}
