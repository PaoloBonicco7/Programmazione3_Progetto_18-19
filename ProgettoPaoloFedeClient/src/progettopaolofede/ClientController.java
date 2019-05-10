package progettopaolofede;

<<<<<<< HEAD
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
=======
import comunication.Email;
import comunication.EmailHandler;
>>>>>>> refs/remotes/origin/master
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
<<<<<<< HEAD
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/* 
 public class ClientController implements Initializable {
 //ATTENZIONE QUANDO SCOMMENTI, CONTROLLA PEZZO X PEZZO CHE NON INCASINI LE COSE
 private List<String> listaMsg = new ArrayList<String>(); // lista msg

 ObservableList<String> observableList; // per listView

 @FXML
 private TextField clientDestTxtField;
 @FXML
 private TextField clientMsgTextField;
 @FXML
 private ListView listview;

 @FXML
 private void handleButtonAction(ActionEvent event) {
 System.out.println("You clicked me!");
 String messaggio = clientMsgTextField.getText();

 ArrayList<String> destinatario = null;
 destinatario.add(clientMsgTextField.getText());

 // Email email = new Email(0, "utente0", destinatario, "argomento", clientMsgTextField.getText(), "today"); //DESTINATARIO LISTA
 Email email = new Email(0, "utente0", "destinatario", "argomento", clientMsgTextField.getText(), "today");
 listaMsg.add(email.toString()); //aggiungo email ad arrayList
 observableList = FXCollections.observableList(listaMsg); //creo observableList e riempio con array di msg
 listview.setItems(observableList);//aggiungo oggetto a listViews GUI

 }

 @FXML
 private void writeMsg(ActionEvent event) {
 try {
 Socket s = new Socket("localhost", 5000); //localhost
 try {
 //               
 //                 InputStream inStream = s.getInputStream(); //flusso in uscita da server a client
 //                 OutputStream outStream = s.getOutputStream();//flusso in uscita da client a server
 //                 PrintWriter out = new PrintWriter(outStream, true); //oggetto per inviare oggetti a server da client..
 //                 Scanner in = new Scanner(inStream); //oggetto per prendere oggetti inviati da server a client
 //
 //                 out.println("client scrive a server");
 //                 

 Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente
 ArrayList<String> destinatario = new ArrayList<>();
 destinatario.add(clientMsgTextField.getText());
 Email email = new Email(0, "utente0", "destinatario", "argomento", clientMsgTextField.getText(), cal.getTime().toString());
 //           EmailHandler emailHandler= new EmailHandler(email,"WRITE");
 ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
 //                out.writeObject(emailHandler);
 out.close();
 } finally {
 s.close();
 }
 } catch (IOException e) {
 System.out.println("ERRORE");
 e.printStackTrace();
 }
 }

 @Override
 public void initialize(URL url, ResourceBundle rb) {
 // TODO
 }

 }
 */
public class ClientController implements Initializable, Serializable {

    private DataModel model; //model del client

    private String Action; //rappresenta azione da eseguire
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model;
        model.loadData();
    }

    @FXML
    private void modifyList(ActionEvent event) {
        //  Email e=new Email(0,"provo","provo","provo","provo","provo");
        Calendar cal = Calendar.getInstance();
        Email email = new Email(0, textFieldFrom.getText(), new ArrayList<String>() {
            {
                add(textFieldTo.getText());
            }
        }, textFieldObject.getText(), textArea.getText(), cal.getTime().toString());
        model.modifyEmailList(email);
    }

    @FXML
    private void sendMsg(ActionEvent event) {
        try {
            Socket s = new Socket("localhost", 5000); //localhost
            try {
                //               
                //                 InputStream inStream = s.getInputStream(); //flusso in uscita da server a client
                //                 OutputStream outStream = s.getOutputStream();//flusso in uscita da client a server
                //                 PrintWriter out = new PrintWriter(outStream, true); //oggetto per inviare oggetti a server da client..
                //                 Scanner in = new Scanner(inStream); //oggetto per prendere oggetti inviati da server a client
                //
                //                 out.println("client scrive a server");
                //                 

                Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente

//                Email email = new Email(0, textFieldFrom.getText(), new ArrayList<String>() {
//                    {
//                        add(textFieldTo.getText());
//                    }
//                }, textFieldObject.getText(), textArea.getText(), cal.getTime().toString());
                //
                Email email = new Email(0, "PROVA", new ArrayList<String>() {
                    {
                        add("PROVA");
                    }
                }, "PROVA", "PROVA", "PROVA");
                // EmailHandler emailHandler= new EmailHandler(email,"WRITE");
                EmailManager emailManager = new EmailManager(email, "SEND");
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

                out.writeObject(emailManager);
=======
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private List<String> listaMsg = new ArrayList<String>(); // lista msg

    ObservableList<String> observableList; // per listView

    @FXML
    private TextField clientDestTxtField;
    @FXML
    private TextField clientMsgTextField;
    @FXML
    private ListView listview;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        String messaggio = clientMsgTextField.getText();

        String destinatario = clientMsgTextField.getText();

        Email email = new Email(0, "utente0", destinatario, "argomento", clientMsgTextField.getText(), "today");

        listaMsg.add(email.toString()); //aggiungo email ad arrayList
        observableList = FXCollections.observableList(listaMsg); //creo observableList e riempio con array di msg
        listview.setItems(observableList);//aggiungo oggetto a listViews GUI

    }

    @FXML
    private void writeMsg(ActionEvent event) {
        try {
            Socket s = new Socket("localhost", 5000); //localhost
            try {
                /*
                 InputStream inStream = s.getInputStream(); //flusso in uscita da server a client
                 OutputStream outStream = s.getOutputStream();//flusso in uscita da client a server
                 PrintWriter out = new PrintWriter(outStream, true); //oggetto per inviare oggetti a server da client..
                 Scanner in = new Scanner(inStream); //oggetto per prendere oggetti inviati da server a client

                 out.println("client scrive a server");
                 */

                Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente
                String destinatario = clientMsgTextField.getText();

                Email email = new Email(0, "utente0", destinatario, "argomento", clientMsgTextField.getText(), cal.getTime().toString());
                EmailHandler emailHandler= new EmailHandler(email,"WRITE");

                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                out.writeObject(emailHandler);
>>>>>>> refs/remotes/origin/master
                out.close();
            } finally {
                s.close();
            }
        } catch (IOException e) {
            System.out.println("ERRORE");
            e.printStackTrace();
        }
<<<<<<< HEAD

    }
=======
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

>>>>>>> refs/remotes/origin/master
}
