import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread implements Runnable {
    int port;
    private ServerSocket serverSocket;

    public ServerThread(int port) throws IOException {
        //Creates server
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        System.out.println("Server Listening on port: " + port);

        //Constantly listens for new clients and adds them
        while(true){
            //Initializes a default socket
            Socket socket = null;
            try {
                //Adds client
                socket = serverSocket.accept();
                System.out.println("New Client: Port: "  + socket.getPort() + " IP:" + socket.getInetAddress());
            } catch (IOException e) {
                System.out.println("Connection Terminated");
            }
            //Assigns new thread for every client
            ClientHandler handler = new ClientHandler(socket);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }

}
