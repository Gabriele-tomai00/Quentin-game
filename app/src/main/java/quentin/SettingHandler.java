package quentin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import quentin.exceptions.CacheDirectoryException;

public class SettingHandler {
    private static final String GAME_DIR = System.getProperty("user.home") + "/.quentinGame";
    private static final String SETTING_FILE = GAME_DIR + "/setting.dat";

    private final Properties settings;

    public static final String USERNAME = "username";
    public static final String DEFAULT = "default";
    public static final String TCP_PORT = "TCP_port";
    public static final String GAMES_WON = "games_won";
    public static final String GAMES_LOST = "games_lost";

    public SettingHandler() {
        settings = new Properties();

        File cacheDirectory = new File(GAME_DIR);
        if (!cacheDirectory.exists() && !cacheDirectory.mkdirs()) {
            throw new CacheDirectoryException("Failed to create cache directory: " + GAME_DIR);
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
            settings.setProperty(USERNAME, DEFAULT);
            settings.setProperty(TCP_PORT, "6789");
            settings.setProperty(GAMES_WON, "0");
            settings.setProperty(GAMES_LOST, "0");
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
        return settings.getProperty(USERNAME, DEFAULT);
    }

    public void setUsername(String username) {
        if (!DEFAULT.equalsIgnoreCase(username)) {
            settings.setProperty(USERNAME, username);
            saveSettings();
        } else {
            throw new IllegalArgumentException("Username cannot be 'default'.");
        }
    }

    public int getPort() {
        return Integer.parseInt(settings.getProperty(TCP_PORT, "8080"));
    }

    public void setPort(int port) {
        settings.setProperty(TCP_PORT, String.valueOf(port));
        saveSettings();
    }

    public int getGamesWon() {
        return Integer.parseInt(settings.getProperty(GAMES_WON, "0"));
    }

    public void setGamesWon(int gamesWon) {
        settings.setProperty(GAMES_WON, String.valueOf(gamesWon));
        saveSettings();
    }

    public void incrementGamesWon() {
        int gamesWon = Integer.parseInt(settings.getProperty(GAMES_WON, "0"));
        gamesWon++;
        settings.setProperty(GAMES_WON, Integer.toString(gamesWon));
        saveSettings();
    }

    public int getGamesLost() {
        return Integer.parseInt(settings.getProperty(GAMES_LOST, "0"));
    }

    public void setGamesLost(int gamesLost) {
        settings.setProperty(GAMES_LOST, String.valueOf(gamesLost));
        saveSettings();
    }

    public void incrementGamesLost() {
        int gamesLost = Integer.parseInt(settings.getProperty(GAMES_LOST, "0"));
        gamesLost++;
        settings.setProperty(GAMES_LOST, Integer.toString(gamesLost));
        saveSettings();
    }

    public void validateUsername() {
        if (DEFAULT.equalsIgnoreCase(getUsername())) {
            throw new IllegalStateException(
                    "Username cannot be 'default'. Please set a valid username.");
        }
    }
}
