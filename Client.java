import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception{
        Socket socket = new Socket("127.0.0.1", 1234); //Connects client to server
        PrintWriter out = new PrintWriter(socket.getOutputStream()); //Output to server

        //Scene setup
        stage.setTitle("Client V3.0");
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 30, 30, 30));

        Scene scene = new Scene(grid, 400, 400);
        stage.setScene(scene);

        TextArea textArea = new TextArea();
        grid.add(textArea, 0, 0, 1, 2);

        Label userLabel = new Label("Username:");
        grid.add(userLabel, 0, 2);

        TextField userTF = new TextField();
        grid.add(userTF, 0, 3);

        Label msgLabel = new Label("Message:");
        grid.add(msgLabel, 0, 4);

        TextField msgTF = new TextField();
        grid.add(msgTF, 0, 5);

        Button sendButton = new Button("Send");
        grid.add(sendButton, 0, 6);

        //Button to send data to server
        sendButton.setOnAction(event -> {
            //Gets user input
            String message = userTF.getText() + ": " + msgTF.getText();
            System.out.println("Send: " + message);
            //Outputs to server
            out.println(message);
            out.flush();
            //Reads previously sent msgs and appends to text area
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                textArea.appendText(in.readLine() + "\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            //Displays the msg you sent
            textArea.appendText("You: " + msgTF.getText() + "\n");
        });

        Button exitButton = new Button("Exit");
        grid.add(exitButton, 0, 7);
        exitButton.setOnAction(event -> {
            System.exit(0);
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
