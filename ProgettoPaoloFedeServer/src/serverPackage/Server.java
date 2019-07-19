package serverPackage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Server extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //  Parent root = FXMLLoader.load(getClass().getResource("server-progetto.fxml")); 
        BorderPane root = new BorderPane();
        FXMLLoader serverLoader = new FXMLLoader(getClass().getResource("server.fxml"));
        root.setCenter(serverLoader.load());

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        ServerController serverController = serverLoader.getController();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                serverController.shutDown();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
