package org.twindev.minecraftlib.logger;

public enum MinecraftLogLevels {

    INFO(""), SUCCESS("<green>"), ERROR("<red>");

    final String color;

    MinecraftLogLevels(final String color) {
        this.color = color;
    }

}
