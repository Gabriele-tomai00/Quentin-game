package quentin.network;

import java.io.IOException;

public interface TcpCliSerInterface {

    void start();

    void stop();

    void sendMessage(String message) throws IOException;

    void listenForMessages() throws IOException;
}
