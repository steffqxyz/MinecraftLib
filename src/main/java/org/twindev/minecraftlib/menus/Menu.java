package org.twindev.minecraftlib.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.utils.MiniColor;

import java.util.List;

public final class Menu implements InventoryHolder {

    private final String title;
    private final List<String> pattern;
    private final List<MenuItem> items;
    private final Inventory inventory;

    public Menu(String title, List<String> pattern, List<MenuItem> items) {
        this.title = title;
        this.pattern = pattern;
        this.items = items;
        inventory = Bukkit.createInventory(this, 9 * pattern.size(), MiniColor.ALL.deserialize(title));
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void setItem(MenuItem item, int i) {
        items.add(item);
        ItemStack stack = item.getStack();
        inventory.setItem(i, stack);
    }

    public int getRows() {
        return pattern.size();
    }

    public List<String> getPattern() {
        return pattern;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
