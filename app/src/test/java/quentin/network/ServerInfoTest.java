package quentin.network;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;

class ServerInfoTest {

    @Test
    void testToString() throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        NetworkInfo info = new NetworkInfo(host, "aleq");
        String expectedString = InetAddress.getLocalHost().getHostAddress() + " - aleq";
        assertTrue(info.toString().contains(expectedString));
    }
}
