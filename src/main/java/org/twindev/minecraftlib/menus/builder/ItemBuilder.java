package org.twindev.minecraftlib.menus.builder;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.utils.Base64Utils;
import org.twindev.minecraftlib.utils.ListUtils;
import org.twindev.minecraftlib.utils.SlotBuilder;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private String material;
    private String displayName;
    private List<String> lore;
    private List<String> leftClickActions;
    private List<String> rightClickActions;
    private List<Integer> slots;
    private String id;
    private boolean paginated = false;


    public ItemBuilder(YamlConfig config, String path) {
        material = config.options().getString(path + ".material", "STONE");
        displayName = config.options().getString(path + ".name", null);
        lore = ListUtils.build(config, path + ".lore");
        slots = SlotBuilder.build(config, path + ".slot");
        leftClickActions = ListUtils.build(config, path + ".left-click-actions");
        rightClickActions = ListUtils.build(config, path + ".right-click-actions");
    }

    private ItemBuilder(@NotNull final String material) { this.material = material; }

    public ItemBuilder copy() {
        final ItemBuilder newBuilder = new ItemBuilder(material);
        if (id != null) newBuilder.id = id;
        if (displayName != null) newBuilder.displayName = displayName;
        if (lore != null) newBuilder.lore = new ArrayList<>(lore);
        if (leftClickActions != null) newBuilder.leftClickActions = new ArrayList<>(leftClickActions);
        if (rightClickActions != null) newBuilder.rightClickActions = new ArrayList<>(rightClickActions);
        if (slots != null) newBuilder.slots = new ArrayList<>(slots);
        return newBuilder;
    }

    public ItemStack build(Player player) {
        ItemStack item;
        try {
            item = new ItemStack(Material.valueOf(material));
        }catch (IllegalArgumentException e) {
            item = new ItemStack(Material.STONE);
        }


        final ItemMeta meta = item.getItemMeta();
        assert meta != null;

        if (displayName != null){
            String display = displayName;
            if (player != null) display = PlaceholderAPI.setPlaceholders(player, display);
            meta.setDisplayName(
                    ChatColor.translateAlternateColorCodes('&', display)
            );
        }

        if (!lore.isEmpty())
            meta.setLore(
                    ListUtils.parseColors(ListUtils.parsePlaceholders(lore, player))
            );



        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (!(leftClickActions == null || leftClickActions.isEmpty())) {
            pdc.set(
                    MinecraftLib.getNamespacedKeyLeft(),
                    PersistentDataType.STRING,
                    Base64Utils.encode(leftClickActions)
            );
        }

        if (!(rightClickActions == null || rightClickActions.isEmpty())) {
            pdc.set(
                    MinecraftLib.getNamespacedKeyRight(),
                    PersistentDataType.STRING,
                    Base64Utils.encode(rightClickActions)
            );
        }

        // WHAT I ADDED //
        if (paginated) {
            pdc.set(
                    MinecraftLib.getNamespacedKeyPaginated(),
                    PersistentDataType.INTEGER,
                    1
            );
        }
        // ^^^^^ //

        item.setItemMeta(meta);
        return item;

    }

    public ItemBuilder setPaginated(final boolean paginated) {
        this.paginated = paginated;
        return this;
    }

    public ItemStack build(){
        return build(null);
    }

    public String id() {
        return id;
    }

    public ItemBuilder setId(String id) {
        this.id = id.toLowerCase();
        return this;
    }

    public ItemBuilder addLeftClickAction(@NotNull final String... action) {
        if (leftClickActions == null) leftClickActions = new ArrayList<>();
        leftClickActions.addAll(List.of(action));
        return this;
    }

    public ItemBuilder addRightClickAction(@NotNull final String... action) {
        if (rightClickActions == null) rightClickActions = new ArrayList<>();
        rightClickActions.addAll(List.of(action));
        return this;
    }

    public List<String> getLeftClickActions() {
        return new ArrayList<>(leftClickActions);
    }

    public List<String> getRightClickActions() {
        return new ArrayList<>(rightClickActions);
    }

    public List<Integer> getSlots() {
        return slots;
    }
    public boolean isPaginated() { return paginated; }

    public ItemBuilder setDisplayName(@NotNull final String displayName) {
        this.displayName = displayName;
        return this;
    }
    public String getDisplayName() { return displayName; }

    public ItemBuilder setLore(@NotNull final List<String> lore) {
        this.lore = lore;
        return this;
    }
    public List<String> getLore() { return lore; }

    public @NotNull String getMaterial () {
        return material;
    }
    public ItemBuilder setMaterial(String material){
        this.material = material;
        return this;
    }

}
