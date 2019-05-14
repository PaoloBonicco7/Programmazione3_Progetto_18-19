package serverPackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private TextArea textAreaMail;
    @FXML
    private TextArea textAreaJson;
    @FXML
    private Button accept; //bottone per avviare connessione
    @FXML
    private Button bottoneScrivi; //bottone che se cliccato il server scrive su json
    @FXML
    private Button bottoneLeggi; //bottone che se cliccato fa leggere al server il file json

    Socket incoming = null;
    @FXML
    public void readJson(){
        String s = null;
        try {
            s = FileEditor.loadFromJson().toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        textAreaJson.setText(s);
    }

    @FXML
    private void handleConnection() { //handle del bottone connetti -> se clicchi si mette in attesa di ricevere connessione. -> VA TOLTO E GESTITO IN ALTRO MODO CONNESSIONE con THREADPOOL

        // L'ide me l'ha fatto cambiare con una lambda i runnable don't ask please
        Runnable run = () -> {
            try {
                ServerSocket s = new ServerSocket(5000);

                while (true) {
                    incoming = s.accept(); // In attesa di connessione

                    ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                    EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
                    Email mail = e.getEmail();

                    ArrayList<String> destinatario = mail.getDestinatario();
                    String mittente = mail.getMittente();

                    Platform.runLater(() -> {
                        //  Gestisco la ricezione della mail e la salvo nel posto "giusto"
                        if (e != null) {
                            try {
                                Map<String, Map<String, Email>> map = FileEditor.loadFromJson();
                                map.get(mittente).put(String.valueOf(destinatario), mail);
                                FileEditor.saveToJson(map);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            String act = e.getAction();

                            String email = "DA: " + mail.getMittente() + " A " + mail.getDestinatario();
                            email = email + "\nOGGETTO: " + mail.getArgomento() + "\n" + mail.getTesto() + "\nData: " + mail.getData();

                            textAreaMail.setText(email);
                            /*
                             *   WRITE = il server quando riceve scrive su json e poi va informato il client destinatario del msg (observable?)
                             *   WRITEALL= direi che possiamo usare solo write e aggiornare piu' client, no?
                             *   REMOVE = il server riceve l'email, la cercanel json e la rimuove
                             *   REPLY = viene creato un oggetto email copiandolo da quelli che vede il cliente e lo spedice al mittente , server fa come write
                             *   REPLY ALL = come reply ma a tutti
                             */

                            try {
                                incoming.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            switch (act) {
                                case "SEND":
                                    // TODO writeHandler
                                    Socket s1 = null;
                                    try {
                                        s1 = new Socket("localhost", 5001); //localhost

                                        ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
                                        out.writeObject(e);
                                        out.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    } finally {
                                        try {
                                            assert s1 != null;
                                            s1.close();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    }
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
                                    break;
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
        //  TODO
    }
}
