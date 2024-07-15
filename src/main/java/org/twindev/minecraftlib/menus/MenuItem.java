package org.twindev.minecraftlib.menus;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.utils.ActionUtils;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.util.List;

public final class MenuItem {

    private Material material;
    private String name;
    private List<String> lore;
    private boolean glow;
    private List<String> viewRequirements;
    private List<String> clickRequirements;
    private List<String> actions;

    private ItemStack stack;

    public MenuItem(YamlConfig config) {

        ConfigurationSection items = config.options().getConfigurationSection("items");
        for (String key : items.getKeys(false)) {
            String material = items.getString(key + ".material");
            this.material = Material.getMaterial(material);
            this.name = items.getString(key + ".name");
            this.lore = items.getStringList(key + ".lore");
            this.glow = items.getBoolean(key + ".glow");
            this.viewRequirements = items.getStringList(key + ".view-requirements");
            this.clickRequirements = items.getStringList(key + ".click-requirements");
            this.actions = items.getStringList(key + ".actions");
        }
        stack = ItemCreator.of(CompMaterial.fromMaterial(this.material)).name(this.name).lore(this.lore).glow(this.glow).make();



    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean canView(Player player) {
        return checkRequirements(player, viewRequirements);
    }

    public boolean canClick(Player player) {
        return checkRequirements(player, clickRequirements);
    }

    private boolean checkRequirements(Player player, List<String> requirements) {
        for (String requirement : requirements) {
            if (requirement.startsWith("[permission]")) {
                String permission = requirement.split(" ")[1];
                if (!player.hasPermission(permission)) {
                    return false;
                }
            } else if (requirement.startsWith("[balance]")) {
                int balance = Integer.parseInt(requirement.split(" ")[1]);
                if (!hasBalance(player, balance)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasBalance(Player player, int requiredBalance) {
        double playerBalance = getPlayerBalance(player);
        return playerBalance >= requiredBalance;
    }

    private double getPlayerBalance(Player player) {
        return MinecraftLib.getEconomy().getBalance(player);
    }

    public void executeActions(Player player) {
        for (String action : actions) {
            if (action.startsWith("[player-command]")) {
                String command = action.split(" ", 2)[1].replace("%player%", player.getName());
                player.performCommand(command);
            } else if (action.startsWith("[message]")) {
                String message = action.split(" ", 2)[1];
                player.sendMessage(message);
            } else if (action.startsWith("[retrieve-balance]")) {
                int amount = Integer.parseInt(action.split(" ")[1]);
                ActionUtils.retrieveBalance(player, amount);
            } else if (action.startsWith("[add-balance]")) {
                int amount = Integer.parseInt(action.split(" ")[1]);
                ActionUtils.addBalance(player, amount);
            } else if (action.startsWith("[sound]")) {
                String sound = action.split(" ")[1];
                ActionUtils.playSound(player, sound);
            } else if (action.startsWith("[title]")) {
                String title = action.split(" ", 2)[1];
                ActionUtils.sendTitle(player, title);
            } else if (action.startsWith("[subtitle]")) {
                String subtitle = action.split(" ", 2)[1];
                ActionUtils.sendSubtitle(player, subtitle);
            } else if (action.startsWith("[actionbar]")) {
                String actionbar = action.split(" ", 2)[1];
                ActionUtils.sendActionBar(player, actionbar);
            } else if (action.startsWith("[broadcast]")) {
                String message = action.split(" ", 2)[1].replace("%player%", player.getName());
                ActionUtils.broadcastMessage(message);
            } else if (action.startsWith("[console-command]")) {
                String command = action.split(" ", 2)[1].replace("%player%", player.getName());
                ActionUtils.runConsoleCommand(command);
            } else if (action.startsWith("[get-item]")) {
                ActionUtils.giveCustomItem(player, stack);
            } else if (action.startsWith("[give]")) {
                String item = action.split(" ", 3)[2];
                ActionUtils.giveItem(player, item);
            }
        }
    }

}
