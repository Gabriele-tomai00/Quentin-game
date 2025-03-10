package quentin.network;

class UdpClientTest {
    //      private static final int PORT = 2000;
    //      private static UdpClient client;
    //      private static Future<NetworkInfo> submit;
    //
    //      @BeforeAll
    //      static void startClient() {
    //        client = new UdpClient(PORT, "test");
    //        ExecutorService executor = Executors.newSingleThreadExecutor();
    //        submit = executor.submit(client::run);
    //      }
    //
    //      @AfterAll
    //      static void stopClient() {
    //        client.stop();
    //      }
    //
    //      @Test
    //      void testClient() throws IOException, InterruptedException, ExecutionException {
    //        try (DatagramSocket datagramSocket = new DatagramSocket(PORT)) {
    //          byte[] buff = new byte[1024];
    //          DatagramPacket packet = new DatagramPacket(buff, buff.length);
    //          datagramSocket.receive(packet);
    //          NetworkInfo info = new NetworkInfo(InetAddress.getByName("localhost"),
    // "serverTest");
    //          byte[] sendBuff = info.getBytes();
    //          packet = new DatagramPacket(sendBuff, sendBuff.length, packet.getAddress(),
    //     packet.getPort());
    //          datagramSocket.send(packet);
    //          // could use a future to obtain data from client -> client = callable
    //          NetworkInfo infoReceived = submit.get();
    //          assertAll(() -> assertEquals(info.address(), infoReceived.address()),
    //                    () -> assertEquals(info.username(), infoReceived.username()));
    //        }
    //      }
    //
    //      class FakeDatagramSocekt extends DatagramSocket {
    //        PipedOutputStream outputStream;
    //
    //        public FakeDatagramSocekt() throws IOException {
    //          outputStream = new PipedOutputStream();
    //          PipedInputStream inputStream = new PipedInputStream(outputStream);
    //
    //        }
    //
    //        @Override
    //        public void send(DatagramPacket p) throws IOException {
    //          outputStream.write(p.getData());
    //        }
    //
    //      }
}
