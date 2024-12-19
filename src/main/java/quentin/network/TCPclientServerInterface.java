package quentin.network;

import java.io.IOException;

public interface TCPclientServerInterface {

  void start();

  void stop();

  void sendMessage(String message) throws IOException;

  void listenForMessages() throws IOException;

  /**
   * Checks if the connection is still active.
   *
   * @return true if the connection is active, false otherwise
   */
  // boolean isConnected();
}
