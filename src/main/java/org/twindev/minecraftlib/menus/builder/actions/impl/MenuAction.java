package org.twindev.minecraftlib.menus.builder.actions.impl;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.menus.Menu;
import org.twindev.minecraftlib.menus.builder.actions.Action;

public class MenuAction extends Action {
    public MenuAction() {
        super("menu");
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String commandLine) {
        commandLine = commandLine.strip();
        String menuId;
        int page = 1;

        if (!commandLine.contains(" ")) menuId = commandLine;
        else {
            final String[] split = commandLine.split(" ");
            menuId = split[0];
            try { page = Integer.parseInt(split[1]); }
            catch (NumberFormatException ignored) {}
        }

        Menu menu = MinecraftLib.getMenuManager().getMenu(menuId);
        if (menu == null) return false;
        menu.open(player, page);

        return true;
    }

}
