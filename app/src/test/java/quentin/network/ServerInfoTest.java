package quentin.network;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ServerInfoTest {
  private ServerInfo info = new ServerInfo("localhost", 1000, "aleq");

  @Test
  void testToString() {
    String expectedString = "localhost - 1000 - aleq";
    assertTrue(info.toString()
                   .contains(expectedString));
  }
}
