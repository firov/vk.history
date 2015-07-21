package org.therg.vk.history.infrastructure;

public class FileUtils {
    public static String escapeFilename(String name) {
        return name.replaceAll("[\"/:*?<>|\\]]", "_").trim();
    }
}
