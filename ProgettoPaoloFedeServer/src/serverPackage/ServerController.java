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
    private UserModel users = new UserModel();
    @FXML
    private TextArea textAreaMail;
    @FXML
    private TextArea titleArea;
    @FXML
    private ToggleButton connectButton;
    private Socket incoming = null;
    private ServerSocket s = null;
    private Socket s1 = null;
    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;
    private boolean online = true;
    private static String ID = "";
    private static int id=0;

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

    /*
     * Remove an email from json file:
     *   first write it on json (overwrite)
     *   and than remove it from json.
     * */
    public void removeMail(Email mail) {
        writeJson(mail);
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

    public void sendMail(EmailManager e, int port) {
        writeJson(e.getEmail());
        try {
            s1 = new Socket("localhost", port); //localhost
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            out.writeObject(e);
            out.close();
        } catch (IOException e1) {
            /* textAreaMail.setText(textAreaMail.getText() + "\n"
             + "Invio effettuato. L'utente destinatario non è attualmente offline. "
             + "\nRiceverà la mail" + "appena sara online.");
             */
        } finally {
            try {
                if (s1 != null) {
                    s1.close();
                    incoming.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void controlloLogin(String msg) throws IOException {
        out = new ObjectOutputStream(incoming.getOutputStream());
        Boolean log = checkLogin(msg);
        if (log) {
            textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + msg + " è loggato.");
        } else {
            textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + msg
                    + " ha provato ad accedere ma il server ha bloccato l'accesso.");
        }
        out.writeObject(log);
    }

    private void loadUserData(User utente) throws IOException {
        out = new ObjectOutputStream(incoming.getOutputStream());
        String nomeUtente = utente.getId();

        Map<String, Email> emails = FileEditor.loadFromJson().get(nomeUtente);
        out.writeObject(emails);

        textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + nomeUtente
                + " ha appena richiesto di scaricare la posta.");

    }

    private void sendHandler(EmailManager e) {
        Email mail = e.getEmail();
        String userMit = mail.getMittente();
        ArrayList<String> userDest = mail.getDestinatario();
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
                        textAreaMail.setText(textAreaMail.getText() + "\n" + "ERRORE CON LA RICERCA DELLA PORTA");
                    }
                }
            }
        }
        //  Se il valore della porta è zero vuol dire che l'utente non è registrato
        if (port == 0) {
            for (User u : list) {
                if (u.getId().equals(mail.getMittente())) {
                    port = u.getPort();
                }
            }
            textAreaMail.setText(textAreaMail.getText() + "\n"
                    + "UTENTE NON REGISTATO");
            userNonEsiste(mail.getMittente(), port); // comunica al client che non esiste il mittente
        }
    }

    private void mailBox(EmailManager e) throws IOException {
        Email mail = e.getEmail();
        ID = Integer.toString (id);
        id++;
        mail.setID(ID);
        if (e != null) {
            String act = e.getAction();
            String userMit = mail.getMittente();
            ArrayList<String> userDest = mail.getDestinatario();
            incoming.close();
            switch (act) {
                case "SEND":
                    sendHandler(e);
                    break;
                case "REMOVE":
                    removeMail(mail);
                    textAreaMail.setText(textAreaMail.getText() + "\n" + "L'utente " + e.getUtente()
                            + " ha appena rimosso una mail dalla sua casella di posta");
                    break;
            }
        }
    }

    private void handlerMail() throws IOException, ClassNotFoundException {
        incoming = s.accept();
        in = new ObjectInputStream(incoming.getInputStream());
        Object receivedMsg = in.readObject();
        titleArea.setText("SERVER \nEstablished connection");

        if (receivedMsg instanceof String) { //controllo di login
            controlloLogin((String) receivedMsg);

        } else if (receivedMsg instanceof User) { //caricamento dati utente
            loadUserData((User) receivedMsg);

        } else { //gestione invio email
            mailBox((EmailManager) receivedMsg);
        }
    }

    // Modifica il testo della textArea per indicare se il server è connesso o no
    public void switcher() {
        if (connectButton.isSelected()) {
            connectButton.setText("SWITCH OFF SERVER");
            Runnable run = () -> {
                try {
                    s = new ServerSocket(5000);
                    titleArea.setText("SERVER \nWaiting for connection");
                    while (true) {
                        handlerMail();
                    }
                } catch (SocketException e2) {  //Eccezione perchè il server ha chiuso connessione
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("ERRORE NEL THREAD DEL SERVER");
                    e.printStackTrace();
                }
            };
            new Thread(run).start();
        } else {
            connectButton.setText("SWITCH ON SERVER");
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void userNonEsiste(String s, int port) {
        Socket s1;
        try {
            s1 = new Socket("localhost", port);
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
            out.writeObject("UTENTE NON ESISTENTE");
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
    }

    /*
     *   Metodo initialize, pulisce l'array degli utenti loggati all'avvio del server.
     **/
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userLog.clear();
    }

    public void shutDown() {
        try {
            online = false;
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("SHUTDOWN\n");
    }
}
