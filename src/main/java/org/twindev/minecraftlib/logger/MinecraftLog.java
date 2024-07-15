package org.twindev.minecraftlib.logger;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.MinecraftLib;
import org.twindev.minecraftlib.utils.MiniColor;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

public class MinecraftLog {

    private static final String PREFIX;
    private static final String DEBUG_PREFIX;
    private static final String DIVIDER;

    private static boolean allowFormatting;
    private static boolean debugMode;

    static {
        PREFIX = "[Astra] ";
        DEBUG_PREFIX = "[<yellow>i<reset>] ";
        DIVIDER = "-----------------------------------";
        allowFormatting = true;
        debugMode = false;
    }

    public static void onLoad() {
        final YamlConfig config = MinecraftLib.getUtility().getConfigYml();
        allowFormatting = config.options().getBoolean("logging.formatting", true);
        debugMode = config.options().getBoolean("logging.debug-mode", false);
    }

    private static void send(@NotNull final MinecraftLogLevels level, final boolean debug, @NotNull final String... input) {
        String prefix = PREFIX;

        if (debug) {
            if (!debugMode) {
                return;
            }

            prefix += DEBUG_PREFIX;
        }

        for (final String i : input) {
            String message = prefix + level.color + i;

            if (!allowFormatting) {
                message = MiniColor.COLOR.strip(message);
            }

            final Component component = MiniColor.COLOR.deserialize(message);
            Bukkit.getConsoleSender().sendMessage(component);
        }
    }

    public static void log(@NotNull final MinecraftLogLevels level, @NotNull final String... input) {
        send(level, false, input);
    }

    public static void log(@NotNull final String... input) {
        send(MinecraftLogLevels.INFO, false, input);
    }

    public static void debug(@NotNull final MinecraftLogLevels level, @NotNull final String... input) {
        send(level, true, input);
    }

    public static void debug(@NotNull final String... input) {
        send(MinecraftLogLevels.INFO, true, input);
    }

    public static void divider(@NotNull final MinecraftLogLevels level, final boolean debug) {
        send(level, debug, DIVIDER);
    }

    public static void divider(@NotNull final MinecraftLogLevels level) {
        send(level, false, DIVIDER);
    }

    public static void divider(final boolean debug) {
        send(MinecraftLogLevels.INFO, debug, DIVIDER);
    }

    public static void divider() {
        send(MinecraftLogLevels.INFO, false, DIVIDER);
    }

}
