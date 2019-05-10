package serverPackage;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import comunication.Email;
import comunication.EmailManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ServerController implements Initializable {

    @FXML
    private TextArea textArea;
    @FXML
    private Button accept; //bottone per avviare connessione
    @FXML
    private Button bottoneScrivi; //bottone che se cliccato il server scrive su json
    @FXML
    private Button bottoneLeggi; //bottone che se cliccato fa leggere al server il file json
    Socket incoming = null;

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
        } catch (Exception/*FileNotFoundException*/ e) {
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
                    });
                }

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    incoming.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
        new Thread(run).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
