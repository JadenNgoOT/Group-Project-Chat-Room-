package Labs.Lab10;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class testUIServer extends Application {
    private TextArea messages = new TextArea();
    private boolean isServer = true;
    private NetworkConnectionTest connection = isServer ? createServer() : createClient();

    public void init() throws Exception{
        connection.startConnection();
    }

    private Parent createUI(){
        messages.setPrefHeight(550);
        TextField input = new TextField();
        input.setOnAction(e -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();
            messages.appendText(message + "\n");
            try {
                connection.send(message);
            } catch (Exception ex) {
                messages.appendText("Message failed to send" + "\n");
            }
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createUI()));
        stage.show();
    }

    public void stop() throws Exception{
        connection.closeConnection();
    }


    private ServerTest createServer(){
        return new ServerTest(1234, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    private ClientTest createClient(){
        return new ClientTest(1234, "127.0.0.1", data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
