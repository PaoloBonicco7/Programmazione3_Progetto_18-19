package serverPackage;

<<<<<<< HEAD

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
=======
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
>>>>>>> refs/remotes/origin/master
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
<<<<<<< HEAD
import java.util.Scanner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
=======
>>>>>>> refs/remotes/origin/master

public class ServerController implements Initializable {

    @FXML
<<<<<<< HEAD
    private TextArea textArea;
    @FXML
    private Button accept; //bottone per avviare connessione
=======
    private Label label;
    @FXML
    private TextArea textArea;
    @FXML
    private Button accept;
>>>>>>> refs/remotes/origin/master
    @FXML
    private Button bottoneScrivi; //bottone che se cliccato il server scrive su json
    @FXML
    private Button bottoneLeggi; //bottone che se cliccato fa leggere al server il file json
<<<<<<< HEAD
    Socket incoming = null;
=======

>>>>>>> refs/remotes/origin/master

    @FXML
    private void handleButtonAction() { // Lettura dal file Json
        try {
            HashMap<Integer, Email> list = FileEditor.loadFromJson();
            System.out.println(list.toString());

            //TODO   Bisogna riuscire a leggere più mail e cercare di capire come accedere ad un singolo campo della classe
            // ad esempio se vogliamo solo il testo della mail ecc.
            /*
             * Adesso leggiamo un oggetto di tipo email dal file json, se però proviamo a mandare più
             * email restituisce un errore perchè (penso) lui si aspetta un oggetto di tipo Email e invece si trova
             * una cazzo di stringa senza senso lunghissima, ma nei porssimi giorni dovrei riuscire a sistemare anche
             * questa cosa.
             * */
<<<<<<< HEAD
        } catch (Exception/*FileNotFoundException*/ e) {
=======

        } catch (FileNotFoundException e) {
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
                    incoming = s.accept(); // In attesa di connessione
                    textArea.setText("COLLEGATO AL CLIENT\n");

                    ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                    EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager

                    Platform.runLater(() -> {
                        // FileEditor.newFile();
                        String act;

                        //if (e != null) {
                        //    HashMap<Integer, Email> map = new HashMap<>();
                        //    map.put(1, e.getEmail());
                        //    try {
                        //       FileEditor.saveToJson(map);
                        //    } catch (IOException e1) {
                        //       e1.printStackTrace();
                        //   }
                        act = e.getAction();
                        /*                      
                         *   WRITE = il server quando riceve scrive su json e poi va informato il client destinatario del msg (observable?)
                         *   WRITEALL= direi che possiamo usare solo write e aggiornare piu' client, no?
                         *   REMOVE = il server riceve l'email, la cercanel json e la rimuove
                         *   REPLY = viene creato un oggetto email copiandolo da quelli che vede il cliente e lo spedice al mittente , server fa come write
                         *   REPLY ALL = come reply ma a tutti
                         */
                        switch (act) {
                            case "SEND":
                                // TODO writeHandler
                                textArea.setText(e.getEmail().toString() + e.getAction());

                                break;

                            case "REMOVE":
                                // TODO removeHandler
                                break;
                            case "REPLY":
                                // TODO replyHandler
                                break;
                            case "REPLYALL":
                                // TODO replyAllHandler
                                break;
                            default:
                                // TODO Inserire azioni di default
                                // Ad esempio il salvataggio delle informazioni in json
                                // o l'aggiornamento di una textArea

                                //       textArea.setText(e.getEmail().toString() + e.getAction());
                                break;
                        }
                        //  }
=======
                    Socket incoming = s.accept(); // In attesa di connessione
                    ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                    EmailHandler e = (EmailHandler) in.readObject(); // UPCAST perchè so che riceverò ogg Email

                    Platform.runLater(() -> {
                        FileEditor.newFile();
                        String act;

                        if (e != null) {
                            HashMap<Integer, Email> map = new HashMap<>();
                            map.put(1, e.getEmail());
                            try {
                                FileEditor.saveToJson(map);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
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
>>>>>>> refs/remotes/origin/master
                    });
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
<<<<<<< HEAD
            } finally {
                try {
                    incoming.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
=======
>>>>>>> refs/remotes/origin/master
            }

        };
        new Thread(run).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
