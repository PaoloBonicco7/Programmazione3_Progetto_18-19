package serverPackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
import javafx.scene.control.ToggleButton;

public class ServerController implements Initializable {

    @FXML
    private TextArea textAreaMail;
    @FXML
    private TextArea titleArea;
    @FXML
    private ToggleButton connectButton;
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
        textAreaMail.setText(s);
    }

    public void writeJson(Email mail){
        ArrayList<String> destinatario = mail.getDestinatario();
        String mittente = mail.getMittente();
        String data = mail.getData();
        try {
            Map<String, Map<String, Email>> map = FileEditor.loadFromJson();
            String key = destinatario + "\n" + data;
            map.get(mittente).put(key, mail);
            FileEditor.saveToJson(map);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void removeMail(Email mail){
        try {
            Map<String, Map<String, Email>> map = FileEditor.loadFromJson();
            String key = mail.getDestinatario() + "\n" + mail.getData();
            map.get(mail.getMittente()).remove(key);
            FileEditor.saveToJson(map);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(EmailManager e){
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
    }

    // Modifica il testo della textArea per indicare se il server è connesso o no
    public void switcher(){
        if(connectButton.isSelected()){
            connectButton.setText("SWITCH OFF SERVER");
        } else {
            connectButton.setText("SWITCH ON SERVER");
        }
    }

    // Su porta 5001 mando a client lista mail, il client la riceve nel metodo initialize
    public void refreshClient(){
        Socket s = null;
        try {
            Map<String, Map<String, Email>> emails = FileEditor.loadFromJson();
            s = new Socket("localhost", 5002); //localhost

            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            out.writeObject(emails);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @FXML
    private void handleConnection() { //handle del bottone connetti -> se clicchi si mette in attesa di ricevere connessione. -> VA TOLTO E GESTITO IN ALTRO MODO CONNESSIONE con THREADPOOL
        switcher();

        refreshClient();

        Runnable run = () -> {
            //  Server manda le mail contenute nel json al client
            try {
                ServerSocket s = new ServerSocket(5000);
                titleArea.setText("SERVER \nWaiting for connection");

                while (true) {
                    incoming = s.accept(); // In attesa di connessione
                    titleArea.setText("SERVER \nEstablished connection");

                    ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                    EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
                    Email mail = e.getEmail();

                    //  Gestisco la ricezione della mail e la salvo nel posto "giusto" su json
                    if (e != null) {
                        writeJson(mail);
                        String act = e.getAction();
                        String email = "DA: " + mail.getMittente() + " A " + mail.getDestinatario();
                        email = email + "\nOGGETTO: " + mail.getArgomento() + "\n" +
                                mail.getTesto() + "\nData: " + mail.getData();

                        textAreaMail.setText(email);

                        //  Chiusura connessione
                        try {
                            incoming.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        switch (act) {
                            case "SEND":
                                // TODO writeHandler
                                sendMail(e);
                                break;
                            case "REMOVE":
                                // TODO removeHandler
                                removeMail(mail);
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
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        };
        new Thread(run).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
