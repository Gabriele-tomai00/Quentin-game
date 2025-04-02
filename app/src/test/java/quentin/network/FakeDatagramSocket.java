package quentin.network;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class FakeDatagramSocket extends DatagramSocket {

    private final PipedOutputStream out;
    private final PipedInputStream in;

    public FakeDatagramSocket() throws IOException {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
    }

    public FakeDatagramSocket(PipedInputStream in, PipedOutputStream out) throws IOException {
        this.in = in;
        this.out = out;
    }

    @Override
    public void send(DatagramPacket p) throws IOException {
        out.write(p.getData());
        out.flush();
    }

    @Override
    public void receive(DatagramPacket p) throws IOException {
        in.read(p.getData());
    }
}
