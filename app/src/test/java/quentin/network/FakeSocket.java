package quentin.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

public class FakeSocket extends Socket {

    private final PipedOutputStream out;
    private final PipedInputStream in;

    public FakeSocket() throws IOException {
        in = new PipedInputStream();
        out = new PipedOutputStream(in);
    }

    public FakeSocket(PipedInputStream in, PipedOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public InputStream getInputStream() {
        return in;
    }

    @Override
    public OutputStream getOutputStream() {
        return out;
    }
}
