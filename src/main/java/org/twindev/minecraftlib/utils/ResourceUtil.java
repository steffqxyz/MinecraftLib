package org.twindev.minecraftlib.utils;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;

public class ResourceUtil {

    public static List<String> listResourceFiles(String folder) throws IOException, URISyntaxException {
        List<String> fileList = new ArrayList<>();
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(folder);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getProtocol().equals("jar")) {
                JarURLConnection jarConnection = (JarURLConnection) resource.openConnection();
                try (JarFile jarFile = jarConnection.getJarFile()) {
                    jarFile.stream()
                            .filter(e -> e.getName().startsWith(folder) && !e.isDirectory())
                            .forEach(e -> fileList.add(e.getName()));
                }
            } else {
                Path resourceFolder = Paths.get(resource.toURI());
                try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourceFolder)) {
                    for (Path path : directoryStream) {
                        fileList.add(folder + "/" + path.getFileName().toString());
                    }
                }
            }
        }
        return fileList;
    }
}
