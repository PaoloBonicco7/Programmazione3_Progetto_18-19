package progettopaolofede;

import com.sun.security.ntlm.Server;
import comunication.Email;
import comunication.EmailManager;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController implements Initializable, Serializable {

    private DataModel model; //model del client

    private String Action; //rappresenta azione da eseguire
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
    private Button sendButton; //bottone invio msg
    @FXML
    private Button replyButton; //bottone reply msg
    @FXML
    private Button newButton; //bottone new msg
    @FXML
    private Button testButton;
    @FXML
    private TextArea textArea2; // Dove arriva il mex dal server
    @FXML
    private Button deleteButton;

    Socket incoming = null;
    private int serverSocket = 5000;
    public ArrayList<Email> emails = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void refresh(){
        ServerSocket s = null;
        try {
            s = new ServerSocket(5001);
            incoming = s.accept();

            ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
            Map<String, Map<String, Email>> map = (Map<String, Map<String, Email>>) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
            emails = new ArrayList<>();

            for(Map.Entry<String, Map<String, Email>> entry : map.entrySet()) {
                Map<String, Email> m = entry.getValue();
                for(Map.Entry<String, Email> entry2 : m.entrySet()) {
                    emails.add((Email) entry2.getValue());
                }
            }

            incoming.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        refresh();
        model.loadData(emails);// inizializzo il model con alcune email per poterci lavorare.
        this.model = model;
        listView.setItems(model.getEmailList());
    }

    public void start() {
        Runnable run = () -> {
            try {
                ServerSocket s = new ServerSocket(5001);
                while (true) {
                    incoming = s.accept();
                    Platform.runLater(() -> {
                        try {
                            ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                            EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
                            Email mail = e.getEmail();

                            model.addEmail(mail);
                            textArea2.setText(mail.getTesto());
                            incoming.close();
                        } catch (IOException | ClassNotFoundException ex) {
                            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            Socket s = new Socket("localhost", 5000); //localhost
            try {
                //SETTAGGIO PARAMETRI EMAIL
                ArrayList<String> destinatari = new ArrayList<>();
                destinatari.addAll(Arrays.asList(textFieldTo.getText().split(","))); //i destinatari son separati da ,
                Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente
                String time = cal.getTime().toString();
                String mittente = textFieldFrom.getText();
                String object = textFieldObject.getText();
                String text = textArea.getText();

                Email email = new Email("ID", mittente, destinatari, object, text, time);
                EmailManager emailHandler = new EmailManager(email, "SEND");

                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(emailHandler);
                out.close();
            } finally {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("ERRORE");
            e.printStackTrace();
        }
    }

    @FXML
    private void replyMsg(ActionEvent event) {//TODO ID
        Email email = listView.getSelectionModel().getSelectedItem();
        String argomento = email.getArgomento();
        String testo = email.getTesto();
        String mittente = "me";
        textFieldTo.setText("");
        textFieldObject.setText(argomento);
        textFieldFrom.setText(mittente);
        textArea.setText(testo);
    }

    @FXML //TODO
    private void removeMsg(ActionEvent event) {//TODO ID

        Email email = listView.getSelectionModel().getSelectedItem();
        // listView.getItems().remove(item); //oggetto rimosso , solo da listview
        model.getEmailList().remove(email); //oggetto rimosso dal model->si propaga sulla listview

        try {
            Socket s = connect();
            try {

                // Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente
                //String time = cal.getTime().toString();
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
    private void replyAllmsg() {//TODO ID, DEST
        Email email = listView.getSelectionModel().getSelectedItem();
        String elencoDestinatari = "";
        String argomento = email.getArgomento();
        String testo = email.getTesto();
        String mittente = "TODOMIttente";
        ArrayList<String> destinatari = new ArrayList<String>();
        destinatari.add("destinatarioTODO"); //devo aggiungere tutti i destinatari qua dentro.
        //todo
        for (String a : destinatari) {
            elencoDestinatari = elencoDestinatari + "," + a;
        }
        textFieldTo.setText(elencoDestinatari);//destinatari
        textFieldObject.setText(argomento);//Argomento msg
        textFieldFrom.setText(mittente);//mittente msg
        textArea.setText(testo);//testo msg
    }

}
