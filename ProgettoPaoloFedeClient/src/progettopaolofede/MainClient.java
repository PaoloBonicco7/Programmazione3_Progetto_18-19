package progettopaolofede;

import comunication.User;
import comunication.UserModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainClient extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        root.setCenter(loginLoader.load());

        UserModel userModel = new UserModel(); //creo il model contenente i 3 utenti (il costruttore non prende parametri ma inizializza di default 3 utenti come li ho definiti io)
        ArrayList<User> listaUtenti = userModel.getListaUtenti(); //prendo la lista di 3 utenti
        LoginController loginController = loginLoader.getController();
        loginController.init(listaUtenti, stage);//passo la lista di utenti x choiceBox e stage per creazione nuova finestra
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        /*
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                ClientController.shutDown();

                Platform.exit();
                System.exit(0);
            }
        });
        */
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
