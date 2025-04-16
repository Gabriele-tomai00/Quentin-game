package quentin.network;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test; // Test of JUnit 5

class SettingHandlerTest {

    @Test
    void testSetAndGetUsername() {
        SettingHandler settingHandler = new SettingHandler();

        settingHandler.setUsername("TestUser");
        assertEquals("TestUser", settingHandler.getUsername());
    }

    @Test
    void testSetUsernameDefaultThrowsException() {
        SettingHandler settingHandler = new SettingHandler();
        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> settingHandler.setUsername("default"));
        assertEquals("Username cannot be 'default'.", exception.getMessage());
    }

    @Test
    void testSetAndGetPort() {
        SettingHandler settingHandler = new SettingHandler();

        settingHandler.setPort(12345);
        assertEquals(12345, settingHandler.getPort());
    }
}
