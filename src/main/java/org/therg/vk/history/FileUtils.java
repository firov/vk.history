package org.therg.vk.history;

public class FileUtils {
    public static String escapeFilename(String name) {
        return name.replaceAll("[\"/:*?<>|\\]]", "_");
    }
}
