package Assignments.GroupProject;

import java.io.*;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try {
            while(true) {
                //Input from the user
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //Output to the user
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                String entry = in.readLine(); //Reads entry from the user
                System.out.println("Recieved: " + entry);
                //Scans the text area of the server
                Scanner scanner = new Scanner(Server.textArea.getText());
                String line = "";
                //Gets the last line from the server
                while (scanner.hasNextLine()){
                    line = scanner.nextLine();
                }
                //Outputs the last line to the client
                //this way the client has a history of messages sent from the other clients
                out.println(line + "\n");
                System.out.println("Sending " + line);
                //Adds new client message to server textarea
                Server.displayMessageServer(entry + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
