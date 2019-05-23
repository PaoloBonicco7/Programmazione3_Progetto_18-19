package progettopaolofede;

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

    private int serverSocket = 5000;
    Socket incoming = null;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /*
     *Metodo che viene invocato in ClientController.initModel();
     *Restituisce la lista di email salvate nel json dal server per lo specifico utente che si collega
     *Si collega al server attraverso il socket alla porta "serverSocket" e invia in output al server
     *una stringa //ora casuale ma sarà il suo nome//, il server nel metodo HandleConnection quando ricevete
     *un oggetto esegue un if , controlla che se è di tipo EmailManager esegue il normale metodo implementato
     *quindi fa send/remove/ecc, se invece il tipo dell'oggetto inviato è "String", legge dal json le email salvate
     * //per ora legge a caso credo// e le reinvia al client. Qui il client le legge e le va aggiungere al model nel metodo 
     *initModel dove termina la chiamata di questo metodo che restituisce l'arraylist di email contenute nel json.
     */
    public ArrayList<Email> refresh() {
        ArrayList<Email> emails = null;

        try {
            Socket s = new Socket("localhost", serverSocket); //localhost
            try {
                String loadData = "LoadEmails"; //Gli passo l'utente così controlla se è già loggato
                out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(loadData);

                in = new ObjectInputStream(s.getInputStream());
                Map<String, Map<String, Email>> map = (Map<String, Map<String, Email>>) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
                emails = new ArrayList<>();
                for (Map.Entry<String, Map<String, Email>> entry : map.entrySet()) {
                    Map<String, Email> m = entry.getValue();
                    for (Map.Entry<String, Email> entry2 : m.entrySet()) {
                        emails.add(entry2.getValue());
                    }
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
            System.out.println("ERRORE");
            e.printStackTrace();
        }
        return emails;
    }

//tengo il metodo qua sotto nel caso di futuri problemi con la mia implementazione di refresh.
    /* 
     public ArrayList<Email> refresh() {
     Socket incoming = null;
     ServerSocket s = null;
     ObjectInputStream in = null;

     ArrayList<Email> emails = null;
     try {
     s = new ServerSocket(5002);
     incoming = s.accept();

     in = new ObjectInputStream(incoming.getInputStream());
     Map<String, Map<String, Email>> map = (Map<String, Map<String, Email>>) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
     emails = new ArrayList<>();

     for (Map.Entry<String, Map<String, Email>> entry : map.entrySet()) {
     Map<String, Email> m = entry.getValue();
     for (Map.Entry<String, Email> entry2 : m.entrySet()) {
     emails.add(entry2.getValue());
     }
     }

     } catch (IOException e) {
     e.printStackTrace();
     } catch (ClassNotFoundException e) {
     e.printStackTrace();
     } finally {
     try {
     in.close(); //chiudo inputStream
     incoming.close();//chiudo socket 
     s.close(); //chiudo serverSocket
     System.out.println("Chiuso inputStream in, Socketincoming e server Socket in refresh() clientController");
     } catch (IOException e) {
     e.printStackTrace();
     }
     return emails;
     }
     }
     */

    @FXML
    public void initModel(DataModel model, String utente) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        userTextArea.setText(utente);
        System.out.println("INVOCO METODO REFRESH()");
        ArrayList<Email> emails = refresh();
        model.loadData(emails);
        this.model = model;
        listView.setItems(model.getEmailList());
        System.out.println("Aggiornato la listVIew e il model");
    }

    //metodo del client che rimane in attesa di ricevere email dal server.
    public void start() {

        Runnable run = () -> {
            ServerSocket s = null;
            try {
                s = new ServerSocket(5001);
                while (true) {
                    incoming = s.accept();
                    Platform.runLater(() -> {
                        try {
                            in = new ObjectInputStream(incoming.getInputStream());
                            EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
                            Email mail = e.getEmail();
                            model.addEmail(mail);
                            textArea2.setText(mail.getTesto());
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
                    System.out.println("chiuso , inpuntStream in , socket incoming e server socket in metodo Start client-controller");
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
                disconnect(s);
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
