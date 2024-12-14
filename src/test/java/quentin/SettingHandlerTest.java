package quentin;

import static org.junit.jupiter.api.Assertions.*; // JUnit 5

import org.junit.jupiter.api.Test; // Test of JUnit 5

public class SettingHandlerTest {

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
            () -> {
              settingHandler.setUsername("default");
            });
    assertEquals("Username cannot be 'default'.", exception.getMessage());
  }

  @Test
  void testSetAndGetPort() {
    SettingHandler settingHandler = new SettingHandler();

    settingHandler.setPort(12345);
    assertEquals(12345, settingHandler.getPort());
  }

  @Test
  void testSetAndGetGamesWon() {
    SettingHandler settingHandler = new SettingHandler();

    settingHandler.setGamesWon(10);
    assertEquals(10, settingHandler.getGamesWon());
  }

  @Test
  void testSetAndGetGamesLost() {
    SettingHandler settingHandler = new SettingHandler();
    settingHandler.setGamesLost(5);
    assertEquals(5, settingHandler.getGamesLost());
  }

  @Test
  void testFilePersistence() throws Exception {
    SettingHandler settingHandler = new SettingHandler();
    settingHandler.setUsername("PersistentUser");
    settingHandler.setPort(9090);
    settingHandler.setGamesWon(20);
    settingHandler.setGamesLost(15);

    // Create a new instance to simulate restarting the application
    SettingHandler newHandler = new SettingHandler();
    assertEquals("PersistentUser", newHandler.getUsername());
    assertEquals(9090, newHandler.getPort());
    assertEquals(20, newHandler.getGamesWon());
    assertEquals(15, newHandler.getGamesLost());
  }

  @Test
  void testValidateUsernamePassesForCustomUsername() {
    SettingHandler settingHandler = new SettingHandler();
    settingHandler.setUsername("CustomUser");
    assertDoesNotThrow(settingHandler::validateUsername);
  }

  @Test
  void testIncrementGamesWon() {
    SettingHandler settingHandler = new SettingHandler();

    settingHandler.setGamesWon(10);
    settingHandler.incrementGamesWon();
    assertEquals(11, settingHandler.getGamesWon());

    settingHandler.setGamesLost(6);
    settingHandler.incrementGamesLost();
    assertEquals(7, settingHandler.getGamesLost());
  }
}
