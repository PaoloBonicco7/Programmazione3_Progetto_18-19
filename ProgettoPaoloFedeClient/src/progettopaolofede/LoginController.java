package progettopaolofede;

import comunication.User;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;

    @FXML
    ImageView image;
    @FXML
    ChoiceBox choiceBox;
    @FXML
    Button login;
    @FXML
    Label loginLabel;

    @FXML
    public void init(ArrayList<User> listaUtenti, Stage stage) {
        this.stage = stage;
        ObservableList<String> list = FXCollections.observableArrayList();
        ArrayList<String> idUtente = new ArrayList<>();
        for (User a : listaUtenti) {
            idUtente.add(a.toString());
        }
        list.addAll(idUtente);
        choiceBox.setItems(list);
    }

    @FXML
    public void loginUser() throws IOException {
        loginLabel.setText("IN ATTESA DI CONNESSIONE CON IL SERVER.... WAIT\n");
        String utente = (String) choiceBox.getSelectionModel().getSelectedItem(); //downCast

        BorderPane root = new BorderPane();
        FXMLLoader sendLoader = new FXMLLoader(getClass().getResource("send.fxml"));
        root.setRight(sendLoader.load());
        ClientController clientController = sendLoader.getController();

        DataModel model = new DataModel();
        clientController.initModel(model, utente);
        clientController.start();   // THREAD CHE SI METTE IN ATTESA DI RICEVERE MAIL DAL SERVER
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
