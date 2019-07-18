package progettopaolofede;

import comunication.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.application.Platform;

public class LoginController {

    private Stage stage;
    private int serverSocket = 5000;
    Socket incoming = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    ArrayList<User> userList; //passato come parametro a init-> è la lista utenti creata dal model
    User loggedUser = null; //user associato all'account loggato

    @FXML
    ImageView image;
    @FXML
    ChoiceBox choiceBox;
    @FXML
    Button login;
    @FXML
    Label loginLabel;

    @FXML
    public void init(ArrayList<User> userList, Stage stage) {
        this.userList = userList;
        this.stage = stage;
        ObservableList<String> list = FXCollections.observableArrayList();
        ArrayList<String> idUtente = new ArrayList<>();
        for (User a : userList) {
            idUtente.add(a.toString());
        }
        list.addAll(idUtente);
        choiceBox.setItems(list);
    }

    private boolean checkLogin(String utente) {
        //Chiedo al server se l'utente è già loggato
        Socket s = null;
        try {
            s = new Socket("localhost", serverSocket); //localhost
            out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(utente);
            in = new ObjectInputStream(s.getInputStream());

            return (Boolean) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            // loginLabel.setText("Impossibile connettersi al server");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("impossibile connettersi al server:");
            alert.setContentText("verificare che il server sia attivo");
            alert.showAndWait();
        } finally {
            try {
                out.close();
               in.close();
                System.out.println("2-close socket checkLogin; \n"); //REMOVEEE
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @FXML
    @SuppressWarnings("Convert2Lambda")
    private void loginUser() throws IOException {
        loginLabel.setText("IN ATTESA DI CONNESSIONE CON IL SERVER.... WAIT\n");
        String utente = (String) choiceBox.getSelectionModel().getSelectedItem(); //downCast
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(utente)) { //controllo lista utenti e utente
                loggedUser = userList.get(i);
            }
        }
        if (checkLogin(utente)) {
            BorderPane root = new BorderPane();
            FXMLLoader clientLoader = new FXMLLoader(getClass().getResource("client.fxml"));
            root.setRight(clientLoader.load());
            ClientController clientController = clientLoader.getController();
            DataModel model = new DataModel();
            clientController.initModel(model, utente, loggedUser, userList);
            clientController.start();   // THREAD CHE SI METTE IN ATTESA DI RICEVERE MAIL DAL SERVER
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    clientController.shutDown();
                    Platform.exit();
                    System.exit(0);
                }
            });
        } else {
            loginLabel.setText("Richiesta di login negata, utente già connesso con questa mail");
        }
    }
}
