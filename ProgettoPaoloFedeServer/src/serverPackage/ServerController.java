
/*
 Server deve aprire PRIMa InputStreamObject, poi Output stream.
 Nel server Il socket non va chiuso, la connessione in e out va chiusa solo nel finally=> NON SO Xk ma da reset conn

 Il client invece apre
 ASSSOLUTAMENTE aprire PRIMA ObjectOUT e poi ObjectInputStream altrimenti exception CONNECTION RESET"
 */
package serverPackage;

import comunication.Email;
import comunication.EmailManager;
import comunication.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

public class ServerController implements Initializable {

    private ArrayList<String> userLog = new ArrayList<>();

    @FXML
    private TextArea textAreaMail;
    @FXML
    private TextArea titleArea;
    @FXML
    private ToggleButton connectButton;

    //queste variabili sono messe qua per poterle usare nel blocco finally
    Socket incoming = null;
    ServerSocket s = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;

    @FXML
    public void readJson() {
        String s = null;
        try {
            s = FileEditor.loadFromJson().toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        textAreaMail.setText(s);
    }

    public void writeJson(Email mail) {
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

    public void removeMail(Email mail) {
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

    public void sendMail(EmailManager e) {
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
    public void switcher() {
        if (connectButton.isSelected()) {
            connectButton.setText("SWITCH OFF SERVER");
        } else {
            connectButton.setText("SWITCH ON SERVER");
        }
    }

    //  Metodo che controlla se un utente si è già connesso
    public boolean checkLogin(String mex) {
        if (!userLog.contains(mex)) {
            userLog.add(mex);
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void handleConnection() { //handle del bottone connetti -> se clicchi si mette in attesa di ricevere connessione. -> VA TOLTO E GESTITO IN ALTRO MODO CONNESSIONE con THREADPOOL
        switcher();
        Runnable run = () -> {
            //  Server manda le mail contenute nel json al client
            try {
                s = new ServerSocket(5000);
                titleArea.setText("SERVER \nWaiting for connection");

                while (true) {
                    incoming = s.accept(); // In attesa di connessione
                    in = new ObjectInputStream(incoming.getInputStream());
                    Object receivedMsg = in.readObject();
                    titleArea.setText("SERVER \nEstablished connection");
                    System.out.println("RICEVUTO OGGETTO, ORA CONTROLLO");

                    if (receivedMsg instanceof String) { //controllo di login                           
                        String msg = (String)receivedMsg;
                        out = new ObjectOutputStream(incoming.getOutputStream());
                        out.writeObject(checkLogin(msg));

                    } else if (receivedMsg instanceof User) {//caricamento dati utente
                        out = new ObjectOutputStream(incoming.getOutputStream());
                        User utente = (User) receivedMsg;
                        String nomeUtente = utente.getId();
                        System.out.println("STAMPO L'utente\n"+nomeUtente);
                        //TODO SELEZIONE EMAIL CORRETTE DA MAP
                        Map<String, Map<String, Email>> emails;
                        emails = FileEditor.loadFromJson();
                        out.writeObject(emails.get(nomeUtente));

                    } else {//gestione invio email
                        EmailManager e = (EmailManager) receivedMsg;
                        Email mail = e.getEmail();

                        //  Gestisco la ricezione della mail e la salvo nel posto "giusto" su json
                        if (e != null) {
                            writeJson(mail);
                            String act = e.getAction();
                            String email = "DA: " + mail.getMittente() + " A " + mail.getDestinatario();
                            email = email + "\nOGGETTO: " + mail.getArgomento() + "\n"
                                    + mail.getTesto() + "\nData: " + mail.getData();

                            textAreaMail.setText(email);

                            //incoming.close();
                            //  Chiusura connessione
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
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("HO UNA CAZ DI ECCEZIONE fuori dal while");
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    incoming.close();
                    s.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
        new Thread(run).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLog.clear();
    }
}
