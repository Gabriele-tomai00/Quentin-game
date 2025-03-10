package quentin.network;

class UdpServerTest {
    //  private static UdpServer server;
    //  private static final int PORT = 9876;

    //  @BeforeAll
    //  static void startServer() {
    //    server = new UdpServer("default", PORT);
    //    new Thread(server::run).start();
    //  }
    //
    //  @AfterAll
    //  static void stopServer() {
    //    server.stop();
    //  }

    //  @Test
    //  void testServer() throws IOException {
    //    try (DatagramSocket datagramSocket = new DatagramSocket()) {
    //      byte[] buff = "mess".getBytes();
    //      DatagramPacket packet = new DatagramPacket(buff, buff.length,
    // InetAddress.getByName("localhost"), PORT);
    //      datagramSocket.send(packet);
    //      buff = new byte[1024];
    //      packet = new DatagramPacket(buff, buff.length);
    //      datagramSocket.receive(packet);
    //      String data = new String(packet.getData(), 0, packet.getLength());
    //      assertEquals(InetAddress.getLocalHost()
    //                              .getHostAddress()
    //          + " - default", data);
    //    }
    //    DatagramSocket clientSocket = new DatagramSocket();
    //    DatagramSocket serverSocket = new DatagramSocket(PORT);
    //    new Thread(() -> {
    //      try {
    //        byte[] buffer = new byte[1024];
    //        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    //        serverSocket.receive(packet);
    //        buffer = "received".getBytes();
    //        packet = new DatagramPacket(buffer, buffer.length, packet.getAddress(),
    // packet.getPort());
    //        serverSocket.send(packet);
    //      } catch (Exception e) {
    //
    //        // TODO: handle exception
    //      }
    //    }).start();
    //    byte[] buffer = "ciao".getBytes();
    //    DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
    // InetAddress.getByName("localhost"), PORT);
    //    clientSocket.send(packet);
    //    buffer = new byte[1024];
    //    packet = new DatagramPacket(buffer, buffer.length);
    //    clientSocket.receive(packet);
    //    String response = new String(packet.getData(), 0, packet.getLength());
    //    assertEquals("received", response);
    //    clientSocket.close();
    //    serverSocket.close();
    //  }
}
