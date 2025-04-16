package quentin.network;

import java.util.Properties;

public class SettingHandler {

    private final Properties settings;

    public static final String USERNAME = "username";
    public static final String DEFAULT = "default";
    public static final String PORT = "port";

    public SettingHandler() {
        settings = new Properties();
        settings.setProperty(USERNAME, DEFAULT);
        settings.setProperty(PORT, "6789");
    }

    public String getUsername() {
        return settings.getProperty(USERNAME, DEFAULT);
    }

    public void setUsername(String username) {
        if (!DEFAULT.equalsIgnoreCase(username)) {
            settings.setProperty(USERNAME, username);
        } else {
            throw new IllegalArgumentException("Username cannot be 'default'.");
        }
    }

    public int getPort() {
        return Integer.parseInt(settings.getProperty(PORT));
    }

    public void setPort(int port) {
        settings.setProperty(PORT, String.valueOf(port));
    }
}
