package com.andreidodu.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO -> create a properties config file instead
public class SettingsService {

    public SettingsService() {
        createDirectoriesIfNotExists();
    }

    public void saveThemeId(int themeIndex) {
        try {
            Files.writeString(getSettingsFilename(), "" + themeIndex, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("cannot save settings.conf: " + e.getMessage());
            // throw new RuntimeException(e);
        }
    }

    public int loadThemeId() {
        try {
            String content = Files.readString(getSettingsFilename(), StandardCharsets.UTF_8);
            return Integer.parseInt(content);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Could not load settings.conf: " + e.getMessage());
            // throw new RuntimeException(e);
        }
        return 0;
    }

    private void createDirectoriesIfNotExists() {
        Path dirPath = getSettingsPath();
        try {
            Files.createDirectories(dirPath);
            System.out.println("Directories created: " + dirPath);
        } catch (IOException e) {
            System.err.println("cannot create directories: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static Path getSettingsPath() {
        String userHome = System.getProperty("user.home");
        Path dirPath = Paths.get(userHome, "justfocus", "settings");
        return dirPath;
    }

    private Path getSettingsFilename() {
        return Paths.get(getSettingsPath().toFile().getAbsolutePath(), "settings.conf");
    }
}
