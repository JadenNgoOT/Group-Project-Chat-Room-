package Labs.Lab10;

import java.io.Serializable;
import java.util.function.Consumer;

public class ClientTest extends NetworkConnectionTest{
    private int port;
    private String ip;

    public ClientTest(int port, String ip, Consumer<Serializable> receiveCallback) {
        super(receiveCallback);
        this.port = port;
        this.ip = ip;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
