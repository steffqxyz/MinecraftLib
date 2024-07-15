package org.twindev.minecraftlib.utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.twindev.minecraftlib.MinecraftLib;

public class ActionUtils {

    public static void retrieveBalance(Player player, double amount) {
        MinecraftLib.getEconomy().withdrawPlayer(player, amount);
    }

    public static void addBalance(Player player, double amount) {
        MinecraftLib.getEconomy().depositPlayer(player, amount);
    }

    public static void playSound(Player player, String sound) {
        player.playSound(player.getLocation(), Sound.valueOf(sound), 0.5f, 0.5f);
    }

    public static void sendTitle(Player player, String title) {
        player.sendTitlePart(TitlePart.TITLE, MiniColor.ALL.deserialize(title));
    }

    public static void sendSubtitle(Player player, String subtitle) {
        player.sendTitlePart(TitlePart.SUBTITLE, MiniColor.ALL.deserialize(subtitle));
    }

    public static void sendActionBar(Player player, String actionbar) {
        player.sendActionBar(MiniColor.ALL.deserialize(actionbar));
    }

    public static void broadcastMessage(String message) {
        Common.broadcast(message);
    }

    public static void runConsoleCommand(String command) {
        Common.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void giveItem(Player player, String item) {
        ItemStack stack = ItemCreator.of(CompMaterial.valueOf(item)).make();
        player.getInventory().addItem(stack);
    }

    public static void giveCustomItem(Player player, ItemStack item) {
        player.getInventory().addItem(item);
    }

}
