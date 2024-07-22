package org.twindev.minecraftlib.utils;

import org.jetbrains.annotations.NotNull;
import org.twindev.minecraftlib.utils.configuration.YamlConfig;

import java.util.ArrayList;
import java.util.List;

public class SlotBuilder {

    public static List<Integer> build(@NotNull final YamlConfig config, @NotNull String path) {
        final List<Integer> slots = new ArrayList<>();
        final List<String> input = ListUtils.build(config, path);

        input.forEach(string -> {
            if (!string.contains("-")) {
                final int slot;
                try { slot = Integer.parseInt(string); }
                catch (NumberFormatException e) { return; }
                if (!slots.contains(slot)) slots.add(slot);
                return;
            }

            final String[] split = string.split("-");
            if (split.length < 2) return;

            final int min, max;
            try {
                final int first, second;
                first = Integer.parseInt(split[0]);
                second = Integer.parseInt(split[1]);
                min = Math.min(first, second);
                max = Math.max(first, second);
            }
            catch (NumberFormatException e) { return; }

            for (int i = min; i <= max; i++)
                if (!slots.contains(i)) slots.add(i);
        });

        return slots;
    }


}
