package serverPackage;

import comunication.Email;
import comunication.EmailHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextArea textArea;
    @FXML
    private Button accept;
    @FXML
    private Button bottoneScrivi; //bottone che se cliccato il server scrive su json
    @FXML
    private Button bottoneLeggi; //bottone che se cliccato fa leggere al server il file json


    @FXML
    private void handleButtonAction() { // Lettura dal file Json
        try {
            ArrayList<Email> list = FileEditor.loadFromJson();
            for (Email ist : list) {
                System.out.println(ist.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleConnection() { //handle del bottone connetti -> se clicchi si mette in attesa di ricevere connessione. -> VA TOLTO E GESTITO IN ALTRO MODO CONNESSIONE con THREADPOOL

        // L'ide me l'ha fatto cambiare con una lambda i runnable don't ask please
        Runnable run = () -> {
            try {
                ServerSocket s = new ServerSocket(5000);

                while (true) {
                    Socket incoming = s.accept(); // In attesa di connessione
                    ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                    EmailHandler e = (EmailHandler) in.readObject(); // UPCAST perchè so che riceverò ogg Email

                    Platform.runLater(() -> {
                        FileEditor.newFile();
                        String act;

                        if (e != null) {
                            FileEditor.saveToJson(e.getEmail());
                            act = e.getAction();

                            /*
                             *   WRITE = il server quando riceve scrive su json e poi va informato il client destinatario del msg (observable?)
                             *   WRITEALL= direi che possiamo usare solo write e aggiornare piu' client, no?
                             *   REMOVE = il server riceve l'email, la cercanel json e la rimuove
                             *   REPLY = viene creato un oggetto email copiandolo da quelli che vede il cliente e lo spedice al mittente , server fa come write
                             *   REPLY ALL = come reply ma a tutti
                             */
                            switch (act) {
                                case "WRITE":
                                    // TODO writeHandler

                                case "WRITEALL":
                                    // TODO writeAllHandler

                                case "REMOVE":
                                    // TODO removeHandler

                                case "REPLY":
                                    // TODO replyHandler

                                case "REPLYALL":
                                    // TODO replyAllHandler

                                default:
                                    // TODO Inserire azioni di default
                                    // Ad esempio il salvataggio delle informazioni in json
                                    // o l'aggiornamento di una textArea

                                    textArea.setText(e.getEmail().toString() + e.getAction());

                            }
                        }
                    });
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        };
        new Thread(run).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
