package quentin.network;

/*
forse si può fare un'interfaccia per tutte le classi che riguardano la rete
 */

/*
quello che potrei fare è:
- faccio il discovery UDP: perpetuo
- se trovo un server: lo devo poter mostrare: mi da l'ip, la porta e il nome utente (flag found=true)
- potrei continuare il discovery all'infinito, finché no scelgo di giocare contro qualcuno
- attivo il clientTCP solo quando ho trovato un server (flag found) tanto nella gui lo mostro solo quando il flag è true
*/

import java.io.IOException;

public class Client {
    private UDPClient udpClient;
    private TCPClient tcpClient;

    public void startDiscovery() throws IOException {
        udpClient = new UDPClient();
        udpClient.setOnDiscoveredCallback(this::linkWithTCPServer);
        udpClient.startDiscovery(); // non-blocking
    }

    public void stopDiscovery() {
        udpClient.stopDiscovery();
    }

    public void linkWithTCPServer() {
        System.out.println("Linking with TCP server...");
        ServerInfo tcpServerInfo = udpClient.getTcpServerInfo();
        if (tcpServerInfo != null) {
            tcpClient = new TCPClient(tcpServerInfo);
            tcpClient.start();
            tcpClient.listenForMessages();
        } else {
            System.out.println("No TCP server information got");
        }
    }

    public void trySendAuthentication(String code) {
        if (code.matches("\\d{5}")) {
            tcpClient.sendMessage(code);
            System.out.println("Password sent to server: " + code);
        }
        System.out.println(
                "Invalid password. Please enter exactly 5 digits. Type again \"clienta\"");
    }

    public void sendMessage(String message) {
        tcpClient.sendMessage(message);
    }
}