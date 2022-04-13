package Labs.Lab10;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientUI extends Application {
    private boolean isServer = false;
    private NetworkConnection connection = isServer ? createServer() : createClient();

    public void init() throws Exception{
        connection.startConnection();
    }

    private Parent createUI(){
        TextField input = new TextField();

        input.setOnAction(e -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();
            try {
                connection.send(message);
            } catch (Exception ex) {

            }
        });

        VBox root = new VBox(20, input);
        root.setPrefSize(600, 600);
        root.setPadding(new Insets(10, 50, 50, 50));
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


    private Server createServer(){
        return new Server(1234, data -> {
            Platform.runLater(() -> {

            });
        });
    }

    private Client createClient(){
        return new Client(1234, "127.0.0.1", data -> {
            Platform.runLater(() -> {
                System.out.println("New Client");
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
