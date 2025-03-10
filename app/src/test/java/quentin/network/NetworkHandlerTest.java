package quentin.network;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.jupiter.api.Test;

import quentin.game.BoardPoint;
import quentin.game.Player;

class NetworkHandlerTest {

  
  @Test
  void testPieRule() throws IOException {
    FakeSocket fakeSocket = new FakeSocket();
    OnlineGame game = new OnlineGame(new Player(BoardPoint.WHITE));
    NetworkHandler handler = new NetworkHandler(fakeSocket, game);
    String commands = "pie\nexit";
    PrintWriter pw = new PrintWriter(fakeSocket.getOutputStream(), true);
    new Thread(handler).start();
    pw.println(commands);
//    handler.run();
    assertEquals(BoardPoint.BLACK, game.getCurrentPlayer()
                                       .color());
  }

}
