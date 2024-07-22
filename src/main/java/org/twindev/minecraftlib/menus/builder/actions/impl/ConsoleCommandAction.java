package org.twindev.minecraftlib.menus.builder.actions.impl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.menus.builder.actions.Action;

public class ConsoleCommandAction extends Action {

    public ConsoleCommandAction() {
        super("player");
    }

    @Override
    public boolean run(@NotNull Player player, @NotNull String commandLine) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
        return true;
    }
}
