package org.twindev.minecraftlib.menus;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.twindev.minecraftlib.utils.ResourceUtil;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public final class MenuManager {

    private final JavaPlugin plugin;
    private List<YamlConfig> configs;
    private Menu menu;

    public MenuManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void loadMenus(String folder) throws IOException, URISyntaxException {
        List<String> fileNames = ResourceUtil.listResourceFiles(folder);
        for (String s : fileNames) {
            if (!s.endsWith(".yml")) return;
            String name = s.substring(0, s.length() - 4);
            new YamlConfig(folder + File.separator + name);
            configs.add(new YamlConfig(folder + File.separator + name));
        }
    }

    public void createMenus() {
        List<MenuItem> items = new ArrayList<>();

        for (YamlConfig config : configs) {
            items.add(new MenuItem(config));
            ConfigurationSection section = config.options().getConfigurationSection("items");

            menu = new Menu(config.options().getString("title"),
                    config.options().getStringList("pattern"),
                    items);

            for (int row = 0; row < menu.getRows(); row++) {
                String[] rowPattern = menu.getPattern().get(row).split(" ");
                for (int col = 0; col < rowPattern.length; col++) {
                    String itemKey = rowPattern[col];
                    if (section.contains(itemKey)) {
                        MenuItem item = new MenuItem(config);
                        menu.setItem(item, row * 9 + col);
                    }
                }
            }
        }
    }

    public List<YamlConfig> getMenuConfigs() {
        return configs;
    }

    public Menu getMenu() {
        return menu;
    }
}
