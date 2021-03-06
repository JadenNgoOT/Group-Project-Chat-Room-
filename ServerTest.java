package Labs.Lab10;

import java.io.Serializable;
import java.util.function.Consumer;

public class ServerTest extends NetworkConnectionTest{
    private int port;

    public ServerTest(int port, Consumer<Serializable> receiveCallback) {
        super(receiveCallback);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
