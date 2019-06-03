package progettopaolofede;

import comunication.Email;
import comunication.EmailManager;
import comunication.User;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class ClientController implements Initializable, Serializable {

    private DataModel model; //model del client

    @FXML
    private ListView<Email> listView;
    @FXML
    private TextField textFieldTo; //campo destinatario
    @FXML
    private TextField textFieldFrom; //campo mittente 
    @FXML
    private TextField textFieldObject; //campo argomento
    @FXML
    private TextArea textArea; //campo testo Msg
    @FXML
    private TextArea textArea2; // Dove arriva il mex dal server
    @FXML
    private TextArea userTextArea;
    private User loggedUser;
    private ArrayList<User> userList;

    private int serverSocket = 5000;
    private Socket incoming = null;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /*
     @FXML
     public static void shoutdown() {
     String logUser = loggedUser.getId();
     Runnable run = () -> {
     //  Server manda le mail contenute nel json al client
     Socket s;
     try {
     s = new Socket("localhost", 5000);

     String u = logUser + "\nd";
     ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
     out.writeObject();
     out.close();

     } catch (UnknownHostException e) {
     e.printStackTrace();
     } catch (IOException e) {
     e.printStackTrace();
     }
     }
     }
     */

    private ArrayList<Email> refresh(User utente) {
        ArrayList<Email> emails = null;
        loggedUser = utente;
        try {
            Socket s = new Socket("localhost", serverSocket); //localhost
            try {
                out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(utente); //loadData

                in = new ObjectInputStream(s.getInputStream());
                Map<String, Email> map = (Map<String, Email>) in.readObject();
                emails = new ArrayList<>();
                for (Map.Entry<String, Email> entry : map.entrySet()) {
                    emails.add(entry.getValue());
                }
            } finally {
                try {
                    out.close();
                    in.close();
                    s.close();
                    System.out.println("Socket chiuso!");
                } catch (IOException exc) {
                    exc.printStackTrace();
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return emails;
    }

    @FXML
    void initModel(DataModel model, String utente, User loggedUser, ArrayList<User> userList) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.userList = userList;
        userTextArea.setText(utente);
        textFieldFrom.setText(utente);
        ArrayList<Email> emails = refresh(loggedUser);
        model.loadData(emails);
        this.model = model;
        listView.setItems(model.getEmailList());
        textFieldFrom.setEditable(false);
    }

    //metodo del client che rimane in attesa di ricevere email dal server.
    void start() {
        Runnable run = () -> {
            ServerSocket s = null;
            try {
                s = new ServerSocket(loggedUser.getPort());
                while (true) {
                    incoming = s.accept();
                    Platform.runLater(() -> {
                        try {
                            in = new ObjectInputStream(incoming.getInputStream());
                            Object objectReceived = in.readObject();
                            if (objectReceived instanceof String) { // il server mi ha segnalato un errore
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error Dialog");
                                alert.setHeaderText("ERROR DIALOG:");
                                alert.setContentText("" + objectReceived + "");
                                alert.showAndWait();
                            } else {
                                EmailManager e = (EmailManager) objectReceived; // UPCAST perchè so che riceverò ogg EmailManager
                                Email mail = e.getEmail();
                                model.addEmail(mail);
                                textArea2.setText(mail.getTesto());
                            }
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {

                    in.close();
                    incoming.close();
                    s.close();
                    System.out.println(" FEDERICO CHIUSO chiuso , inpuntStream in , socket incoming e server socket in metodo Start client-controller");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(run).start();
    }

    private Socket connect() throws IOException {
        return new Socket("localhost", serverSocket);
    }

    private void disconnect(Socket s) {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //METODO INUTILE, USATO IN FASE DI TESTING DI OBSERVABLE-LIST
    @FXML
    private void modifyList(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        Email email = new Email(textFieldFrom.getText(), textFieldFrom.getText(), new ArrayList<String>() {
            {
                add(textFieldTo.getText());
            }
        }, textFieldObject.getText(), textArea.getText(), cal.getTime().toString());
        model.addEmail(email);
    }

    @FXML
    private void sendMsg(ActionEvent event) {//TODO ID
        try {
            Socket s = connect(); //localhost,serversocket
            try {
                ArrayList<String> destinatari = new ArrayList<>(Arrays.asList(textFieldTo.getText().split(",")));
                Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente
                String time = cal.getTime().toString();
                String mittente = textFieldFrom.getText();
                String object = textFieldObject.getText();
                String text = textArea.getText();

                Email email = new Email("ID", mittente, destinatari, object, text, time);
                EmailManager emailHandler = new EmailManager(email, "SEND");
                emailHandler.setPort(loggedUser.getPort());

                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(emailHandler);
                out.close();
            } finally {
                disconnect(s);
            }
        } catch (IOException e) {
            // Gestione perdita connessione
            Platform.runLater(() -> {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("ERROR");

                a.setHeaderText("LOST CONNECTION");
                a.setContentText("Riprovare piu tardi");

                a.showAndWait();//dopo aver schiacciato ok verra eseguito il seguente metodo che mi riporterà al login
            });
        }
    }

    @FXML
    private void replyMsg(ActionEvent event) {//TODO ID
        Email email = listView.getSelectionModel().getSelectedItem();
        String argomento = email.getArgomento();
        String mittente = loggedUser.getId();
        String destinatario = email.getMittente(); //rispondo a chi mi ha inviato la email => 
        textFieldTo.setText(destinatario);//prendo il campo mittente di chi mi ha inviato la mail e diventa il mio destinatario
        textFieldObject.setText("REPLY: " + argomento);
        textFieldFrom.setText(mittente);
        textArea.setText("");
    }

    @FXML //TODO
    private void removeMsg(ActionEvent event) {//TODO ID

        Email email = listView.getSelectionModel().getSelectedItem();
        model.getEmailList().remove(email); //oggetto rimosso dal model->si propaga sulla listview

        try {
            Socket s = connect();
            try {
                String time = email.getData();
                String id = "ID"; //todo
                String mittente = email.getMittente();
                ArrayList<String> destinatari = (ArrayList<String>) email.getDestinatario();//non usato xk serializ error
                String object = email.getArgomento();
                String text = email.getTesto();
                Email e = new Email(id, mittente, destinatari, object, text, time); //usa dest e non destinatari
                EmailManager emailManager = new EmailManager(e, "REMOVE");
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(emailManager);
                out.close();

            } finally {
                disconnect(s);
            }
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    @FXML
    private void replyAllmsg() {
        Email email = listView.getSelectionModel().getSelectedItem();
        String elencoDestinatari = "";
        String argomento = email.getArgomento();
        String mittente = loggedUser.getId();

        for (User user : userList) {
            if (!(user.getId().equals(mittente))) {//inserisco tutti destinatari escluso me
                elencoDestinatari = elencoDestinatari + user.getId() + ",";
            }
        }
        textFieldTo.setText(elencoDestinatari);//destinatari
        textFieldObject.setText(argomento);//Argomento msg
        textFieldFrom.setText(mittente);//mittente msg
        textArea.setText("");//testo msg
    }

    @FXML
    private void newMsg() {
        textFieldTo.setText("");
        textFieldObject.setText("");
        textFieldFrom.setText(loggedUser.getId());
        textArea.setText("");

    }

    @FXML
    private void forward() {
        Email email = listView.getSelectionModel().getSelectedItem();
        textFieldFrom.setText(loggedUser.getId());
        textFieldTo.setText(email.getMittente());
        textFieldObject.setText(email.getArgomento());
        textArea.setText(email.getTesto());

    }

    @FXML
    private void forwardAll() {
        Email email = listView.getSelectionModel().getSelectedItem();
        String mittente = loggedUser.getId();
        String elencoDestinatari = "";

        for (User user : userList) {
            if (!(user.getId().equals(mittente))) {//inserisco tutti destinatari escluso me
                elencoDestinatari = elencoDestinatari + user.getId() + ",";
            }
        }
        textFieldFrom.setText(mittente);
        textFieldObject.setText(email.getArgomento());
        textArea.setText(email.getTesto());
        textFieldTo.setText(elencoDestinatari);

    }

}
