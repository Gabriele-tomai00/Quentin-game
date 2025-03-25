package quentin.network;

import java.util.Properties;

public class SettingHandler {

    private final Properties settings;

    public static final String USERNAME = "username";
    public static final String DEFAULT = "default";
    public static final String TCP_PORT = "tcpPort";

    public SettingHandler() {
        settings = new Properties();
        settings.setProperty(USERNAME, DEFAULT);
        settings.setProperty(TCP_PORT, "6789");
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
        return Integer.parseInt(settings.getProperty(TCP_PORT, "8080"));
    }

    public void setPort(int port) {
        settings.setProperty(TCP_PORT, String.valueOf(port));
    }

    public void validateUsername() {
        if (DEFAULT.equalsIgnoreCase(getUsername())) {
            throw new IllegalStateException(
                    "Username cannot be 'default'. Please set a valid username.");
        }
    }
}
