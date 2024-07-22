package org.twindev.minecraftlib.menus.builder.actions;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.menus.builder.actions.impl.ConsoleCommandAction;
import org.twindev.minecraftlib.menus.builder.actions.impl.MenuAction;
import org.twindev.minecraftlib.menus.builder.actions.impl.MessageAction;
import org.twindev.minecraftlib.menus.builder.actions.impl.PlayerCommandAction;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionManager {

    private static final Map<String, Action> actions = new HashMap<>();
    private static Action defAction;

    // The main class at this point is only there in case on of your actions needs it
    public static void load(JavaPlugin plugin) {
        defAction = new ConsoleCommandAction();
        new PlayerCommandAction();
        new MessageAction();
        new MenuAction();

    }

    protected static void register(Action action) {
        actions.putIfAbsent(action.id(), action);
    }

    public static boolean exists(@NotNull final String id) {
        return actions.containsKey(id);
    }

    public static Action get(@NotNull final String id) {
        return actions.getOrDefault(id, defAction);
    }

    private static final Pattern actionPattern = Pattern.compile("^(\\[)(.*?)(])");

    public static Action fromCommandLine(@NotNull final String commandLine) {
        final Matcher matcher = actionPattern.matcher(commandLine);
        if (!matcher.find() || matcher.groupCount() < 3) return defAction;
        final String id = matcher.group(2);
        return actions.getOrDefault(id, defAction);
    }

    public static String replaceCommandLine(@NotNull String commandLine) {
        final Matcher matcher = actionPattern.matcher(commandLine);
        if (!matcher.find()) return commandLine;
        return commandLine.substring(matcher.end()).strip();
    }
}
