package serverPackage;

import comunication.Email;
import comunication.EmailManager;
import comunication.User;
import comunication.UserModel;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

public class ServerController implements Initializable {

    private ArrayList<String> userLog = new ArrayList<>();

    UserModel users = new UserModel();

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
        String dest = destinatario.get(0);
        String mittente = mail.getMittente();
        String data = mail.getData();
        try {
            Map<String, Map<String, Email>> map = FileEditor.loadFromJson();
            String key = mittente + "\n" + data;
            map.get(dest).put(key, mail);
            FileEditor.saveToJson(map);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void removeMail(Email mail) {
        try {
            Map<String, Map<String, Email>> map = FileEditor.loadFromJson();
            ArrayList<String> destinatario = mail.getDestinatario();
            String dest = destinatario.get(0);
            String mittente = mail.getMittente();
            String data = mail.getData();

            String key = mittente + "\n" + data;
            map.get(dest).remove(key);
            FileEditor.saveToJson(map);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMail(EmailManager e, int port) /*throws Exception*/ {
        Socket s1 = null;
        try {
            s1 = new Socket("localhost", port); //localhost
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            out.writeObject(e);
            out.close();
        } catch (IOException e1) {
            System.out.println("client offline, non è poss inviare msg");
            textAreaMail.setText(textAreaMail.getText() + "\n"
                    + "L'utente non on-line, invio fallito. "
                    + "\nRiceverà la mail" + "appena sara online.");

        } finally {
            try {
                if (s1 != null) {
                    s1.close();
                }
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

    public void disconectUser(String u) {
        userLog.remove(u);
    }

    public void userNonEsiste(String s, int port){
        Socket s1;
        try{
            s1 = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            out.writeObject("UTENTE NON REGISTRATO");
            out.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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

                    if (receivedMsg instanceof String) { //controllo di login
                        String msg = (String) receivedMsg;
                        out = new ObjectOutputStream(incoming.getOutputStream());
                        Boolean log = checkLogin(msg);
                        if (log) {
                            textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + msg + " è loggato.");
                        } else {
                            textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + msg
                                    + " ha provato ad accedere ma il server ha bloccato l'accesso.");
                        }
                        out.writeObject(log);

                    } else if (receivedMsg instanceof User) { //caricamento dati utente
                        out = new ObjectOutputStream(incoming.getOutputStream());
                        User utente = (User) receivedMsg;
                        String nomeUtente = utente.getId();

                        Map<String, Email> emails = FileEditor.loadFromJson().get(nomeUtente);
                        out.writeObject(emails);

                        textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + nomeUtente
                                + " ha appena richiesto di scaricare la posta.");

                    } else { //gestione invio email
                        EmailManager e = (EmailManager) receivedMsg;
                        Email mail = e.getEmail();

                        //  Gestisco la ricezione della mail e la salvo nel posto "giusto" su json
                        if (e != null) {
                            writeJson(mail);
                            String act = e.getAction();
                            String userMit = mail.getMittente();
                            ArrayList<String> userDest = mail.getDestinatario();

                            /*
                             String email = "DA: " + mail.getMittente() + " A " + mail.getDestinatario();
                             email = email + "\nOGGETTO: " + mail.getArgomento() + "\n"
                             + mail.getTesto() + "\nData: " + mail.getData();
                             */
                            //incoming.close();
                            switch (act) {
                                case "SEND":
                                    // TODO writeHandler

                                    ArrayList<User> list = users.getListaUtenti();
                                    int port = 0;

                                    for (String user : userDest) {
                                        for (User u : list) {
                                            if (u.getId().equals(user)) { //utente a cui inviare
                                                port = u.getPort();
                                                sendMail(e, port);
                                                textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente "
                                                        + userMit + " ha appena inviato una mail a " + userDest);
                                                // ERRORE NELLA PORTA
                                                if (port == 0) {
                                                    System.out.println("NON HO RICEVUTO LA PORTA");
                                                    textAreaMail.setText(textAreaMail.getText() + "\n"
                                                            + "ERRORE CON LA RICERCA DELLA PORTA");
                                                }
                                            }
                                        }
                                    }

                                    if (port == 0){
                                        for (User u : list) {
                                            if (u.getId().equals(mail.getMittente())) {
                                                port = u.getPort();
                                            }
                                        }
                                        if (port == 0){
                                            System.out.println("PORCACCIODIO");
                                        }
                                        textAreaMail.setText(textAreaMail.getText() + "\n"
                                                + "UTENTE NON REGISTATO");
                                        userNonEsiste(mail.getMittente(), port); // comunica al client che non esiste il mittente
                                    }

                                    break;

                                case "REMOVE":
                                    // TODO removeHandler
                                    removeMail(mail);
                                    textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + userDest
                                            + " ha appena rimosso una mail dalla sua casella di posta");
                                    break;
                                case "REPLY":
                                    // TODO replyHandler
                                    break;
                                case "REPLYALL":
                                    // TODO replyAllHandler
                                    break;
                                default:
                                    // TODO Inserire azioni di default
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
        new Thread(run)
                .start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLog.clear();
    }
}
