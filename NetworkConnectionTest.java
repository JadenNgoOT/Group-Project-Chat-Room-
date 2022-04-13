package Labs.Lab10;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public abstract class NetworkConnectionTest {
    private Consumer<Serializable> onReceiveCallback;
    private ConnectionThread connectThread = new ConnectionThread();

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    public NetworkConnectionTest(Consumer<Serializable> receiveCallback){
        this.onReceiveCallback = receiveCallback;
        connectThread.setDaemon(true);
    }

    public void startConnection() throws Exception{
        connectThread.start();
    }

    public void closeConnection() throws Exception{
        connectThread.socket.close();
    }

    public void send(Serializable data) throws Exception{
        connectThread.out.writeObject(data);
    }

    private class ConnectionThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;

        public void run(){
            try(ServerSocket server =  isServer() ? new ServerSocket(getPort()) : null;
            Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())){
                this.socket = socket;
                this.out = out;
                socket.setTcpNoDelay(true);

                while(true){
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }
            }
            catch (Exception e){
                onReceiveCallback.accept("Connection terminated");
            }
        }

    }
}
