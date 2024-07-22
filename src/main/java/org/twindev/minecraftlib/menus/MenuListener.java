package org.twindev.minecraftlib.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.menus.builder.actions.Action;
import org.twindev.minecraftlib.menus.builder.actions.ActionManager;
import org.twindev.minecraftlib.utils.Base64Utils;

import java.util.List;
import java.util.Set;

public class MenuListener implements Listener {

    public MenuListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        final Inventory inv = event.getClickedInventory();
        if (inv == null) return;

        final InventoryHolder holder = inv.getHolder();
        final ClickType clickType = event.getClick();

        if (!(holder instanceof Menu)) {
            final Inventory top = event.getView().getTopInventory();
            if (!(top.getHolder() instanceof Menu)) return;

            if (clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT)
                event.setCancelled(true);

            return;
        }

        event.setCancelled(true);
        final ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        final ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        final PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // WHAT I ADDED THIS TIME //

        final Menu menu = (Menu) holder;
        if (menu.isPaginated() && pdc.has(MinecraftLib.getNamespacedKeyPaginated(), PersistentDataType.INTEGER)) {
            menu.paginate().onClick(event);
            return;
        }

        // ^^^^^ //

        List<String> actions = null;

        if (clickType == ClickType.LEFT) {
            final String encoded = pdc.get(MinecraftLib.getNamespacedKeyLeft(), PersistentDataType.STRING);
            if (encoded != null) actions = Base64Utils.decode(encoded);
        }

        else if (clickType == ClickType.RIGHT) {
            final String encoded = pdc.get(MinecraftLib.getNamespacedKeyRight(), PersistentDataType.STRING);
            if (encoded != null) actions = Base64Utils.decode(encoded);
        }

        if (actions == null || actions.isEmpty()) return;
        final Player player = (Player) event.getWhoClicked();

        actions.forEach(commandLine -> {
            final Action action = ActionManager.fromCommandLine(commandLine);
            if (action == null) return;

            commandLine = ActionManager.replaceCommandLine(commandLine);
            action.run(player, commandLine);
        });

    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        final Inventory inv = event.getInventory();
        if (!(inv.getHolder() instanceof Menu)) return;

        final int size = inv.getSize();
        final Set<Integer> slots = event.getRawSlots();

        for (int i = 0; i < size; i++)
            if (slots.contains(i)) {
                event.setCancelled(true);
                break;
            }
    }

}
