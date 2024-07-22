package org.twindev.minecraftlib.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResourceUtil {

    public static List<String> listFilesInMenusFolder(JavaPlugin plugin) {
        List<String> fileNames = new ArrayList<>();
        File menusFolder = new File(plugin.getDataFolder().getAbsolutePath(), "menus");

        // Ensure the menus folder exists
        if (menusFolder.exists() && menusFolder.isDirectory()) {
            // Recursively list all files
            listFilesRecursively(menusFolder, fileNames);
        } else {
            System.out.println("The menus folder does not exist or is not a directory.");
        }

        return fileNames;
    }

    private static void listFilesRecursively(File directory, List<String> fileNames) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    listFilesRecursively(file, fileNames);
                } else {
                    fileNames.add(file.getAbsolutePath());
                }
            }
        }
    }
}
