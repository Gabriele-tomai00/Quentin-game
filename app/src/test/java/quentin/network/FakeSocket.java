package quentin.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.Socket;

public class FakeSocket extends Socket {

    private final PipedOutputStream out = new PipedOutputStream();
    private final PipedInputStream in = new PipedInputStream();

    public FakeSocket() throws IOException {
        out.connect(in);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return in;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return out;
    }
}
