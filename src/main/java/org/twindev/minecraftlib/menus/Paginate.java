package org.twindev.minecraftlib.menus;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.menus.builder.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public abstract class Paginate<T> implements Listener {

    private final JavaPlugin plugin;

    private final List<T> objects;
    private final List<Integer> slots;
    private final ItemBuilder itemBuilder;

    public Paginate(
            JavaPlugin plugin,
            @NotNull final List<T> objects,
            @NotNull final List<Integer> slots,
            @NotNull final ItemBuilder itemBuilder
    ) {
        this.objects = objects;
        this.slots = slots;
        this.itemBuilder = itemBuilder;
        this.plugin = plugin;
    }

    public List<Integer> slots() { return new ArrayList<>(slots); }
    public List<T> objects() { return new ArrayList<>(objects); }
    public ItemBuilder itemBuilder() { return itemBuilder; }

    public int pages() {
        final int size = slots.size();
        if (size == 0) return 1;

        final int objectAmount = objects.size();
        final double result = (double) objectAmount/size;

        int res = (int) Math.ceil(result);
        if (res < 1) res = 1;

        return res;
    }

    public abstract void populate(
            final int page,
            @NotNull final Inventory inventory,
            final OfflinePlayer player
    );

    public void populate(
            final int page,
            @NotNull final Inventory inventory,
            @NotNull final List<ItemStack> items
    ) {
        final int amount = items.size()-1;
        final int usableSlots = slots.size();

        final int start = (page-1)*usableSlots;
        final int end = (page*usableSlots)-1;

        int index = 0;
        for (int i = 0; i < usableSlots; i++) {
            final int result = start+index;
            if (index > end || amount < result) continue;
            inventory.setItem(slots.get(i), items.get(result));
            index++;
        }
    }

    public abstract String replace(@NotNull String input, @NotNull T object);
    public abstract List<String> replace(@NotNull List<String> input, @NotNull T object);

    public abstract void onClick(InventoryClickEvent event);
    public abstract String replaceAction(T object, String commandLine);

}
