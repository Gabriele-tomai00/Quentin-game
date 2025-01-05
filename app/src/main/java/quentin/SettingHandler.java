package quentin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingHandler {
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String SETTING_FILE = GAME_DIR + "/setting.dat";

    private final Properties settings;

    public SettingHandler() {
        settings = new Properties();

        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new RuntimeException("Failed to create cache directory: " + GAME_DIR);
        }
        loadSettings();
    }

    public void loadSettings() {
        File settingFile = new File(SETTING_FILE);
        if (settingFile.exists()) {
            try (FileInputStream fis = new FileInputStream(settingFile)) {
                settings.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading settings: " + e.getMessage());
            }
        } else {
            settings.setProperty("username", "default");
            settings.setProperty("TCP_port", "6789");
            settings.setProperty("games_won", "0");
            settings.setProperty("games_lost", "0");
            saveSettings();
        }
    }

    public void saveSettings() {
        try (FileOutputStream fos = new FileOutputStream(SETTING_FILE)) {
            settings.store(fos, "Quentin Game Settings");
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    public String getUsername() {
        return settings.getProperty("username", "default");
    }

    public void setUsername(String username) {
        if (!"default".equalsIgnoreCase(username)) {
            settings.setProperty("username", username);
            saveSettings();
        } else {
            throw new IllegalArgumentException("Username cannot be 'default'.");
        }
    }

    public int getPort() {
        return Integer.parseInt(settings.getProperty("TCP_port", "8080"));
    }

    public void setPort(int port) {
        settings.setProperty("TCP_port", String.valueOf(port));
        saveSettings();
    }

    public int getGamesWon() {
        return Integer.parseInt(settings.getProperty("games_won", "0"));
    }

    public void setGamesWon(int gamesWon) {
        settings.setProperty("games_won", String.valueOf(gamesWon));
        saveSettings();
    }

    public void incrementGamesWon() {
        int GamesWon = Integer.parseInt(settings.getProperty("games_won", "0"));
        GamesWon++;
        settings.setProperty("games_won", Integer.toString(GamesWon));
        saveSettings();
    }

    public int getGamesLost() {
        return Integer.parseInt(settings.getProperty("games_lost", "0"));
    }

    public void setGamesLost(int gamesLost) {
        settings.setProperty("games_lost", String.valueOf(gamesLost));
        saveSettings();
    }

    public void incrementGamesLost() {
        int gamesLost = Integer.parseInt(settings.getProperty("games_lost", "0"));
        gamesLost++;
        settings.setProperty("games_lost", Integer.toString(gamesLost));
        saveSettings();
    }

    public void validateUsername() {
        if ("default".equalsIgnoreCase(getUsername())) {
            throw new IllegalStateException(
                    "Username cannot be 'default'. Please set a valid username.");
        }
    }
}
