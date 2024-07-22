package org.twindev.minecraftlib.menus.builder.actions;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Action {

    private final String id;

    public Action(@NotNull final String id) {
        this.id = id;
        ActionManager.register(this);
    }

    public String id() { return id; }

    public abstract boolean run(@NotNull final Player player, @NotNull final String commandLine);
}
