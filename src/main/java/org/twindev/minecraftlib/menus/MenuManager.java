package org.twindev.minecraftlib.menus;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {

    private final Map<String, Menu> menus = new HashMap<>();

    protected void register(@NotNull final Menu menu) {
        menus.putIfAbsent(menu.getId(), menu);
    }

    public boolean exists(@NotNull final String id) {
        return menus.containsKey(id);
    }

    public Menu getMenu(@NotNull final String id) {
        return menus.getOrDefault(id, null);
    }

}
