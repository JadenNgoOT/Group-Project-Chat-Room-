package Labs.Lab10;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public abstract class NetworkConnection {
    private static Consumer<Serializable> onReceiveCallback;
    private ConnectionThread connectThread = new ConnectionThread();

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    public NetworkConnection(Consumer<Serializable> receiveCallback){
        this.onReceiveCallback = receiveCallback;
        connectThread.setDaemon(true);
    }

    public void startConnection() throws Exception{
        connectThread.start();
    }

    public void closeConnection() throws Exception{
        connectThread.server.close();
    }

    public void send(Serializable data) throws Exception{
        ClientHandler.out.writeObject(data);
    }

    private class ConnectionThread extends Thread{
        ServerSocket server = null;
        public void run(){
            try {
                if(isServer()){
                    server = new ServerSocket(getPort());
                    server.setReuseAddress(true);
                    while(true){
                        Socket client = server.accept();
                        System.out.println("New client connected"
                                + client.getInetAddress()
                                .getHostAddress());
                        ClientHandler clientSocket = new ClientHandler(client);
                        new Thread(clientSocket).start();
                    }
                }
                else{
                    Socket socket = new Socket(getIP(), getPort());
                }

            }

            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (server != null) {
                    try {
                        server.close();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private static ObjectOutputStream out;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run(){
            try(ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())){
                this.out = out;
                clientSocket.setTcpNoDelay(true);

                while(true){
                    Serializable data = (Serializable) in.readObject();
                    onReceiveCallback.accept(data);
                }

            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
