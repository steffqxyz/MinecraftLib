package org.twindev.minecraftlib.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static List<String> build(YamlConfig config, String path) {
        final Object o = config.options().get(path);
        if (o == null) return new ArrayList<>();

        if (o instanceof List<?>) return config.options().getStringList(path);
        return new ArrayList<>(List.of(o.toString()));
    }
    public static List<String> parsePlaceholders(@NotNull final List<String> input, final Player player) {
        if (player == null) return input;
        final List<String> parsed = new ArrayList<>();
        input.forEach(string ->
                parsed.add(PlaceholderAPI.setPlaceholders(player, string))
        );
        return parsed;
    }
    public static List<String> parseColors(@NotNull final List<String> input) {
        final List<String> parsed = new ArrayList<>();
        input.forEach(string ->
                parsed.add(ChatColor.translateAlternateColorCodes('&', string))
        );
        return parsed;
    }

}

