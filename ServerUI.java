package Labs.Lab10;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;


public class ServerUI extends Application {
    private TextArea messages = new TextArea();
    private Button button = new Button("Exit");
    private boolean isServer = true;
    private NetworkConnection connection = isServer ? createServer() : createClient();

    public void init() throws Exception{
        connection.startConnection();
    }

    private Parent createUI(){
        messages.setPrefHeight(450);

        VBox root = new VBox(50, messages, button);
        root.setPrefSize(800, 600);
        root.setPadding(new Insets(10, 50, 50, 50));
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        button.setOnAction(e-> {
            try {
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(createUI());
        stage.setScene(scene);
        stage.show();
    }

    private Server createServer(){
        return new Server(1234, data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    private Client createClient(){
        return new Client(1234, "127.0.0.1", data -> {
            Platform.runLater(() -> {
                messages.appendText(data.toString() + "\n");
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
