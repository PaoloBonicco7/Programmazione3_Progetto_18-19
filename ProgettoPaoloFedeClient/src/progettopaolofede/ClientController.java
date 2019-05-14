package progettopaolofede;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import comunication.Email;
import comunication.EmailManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/* 
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    @FXML
    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        model.loadData();// inizializzo il model con alcune email per poterci lavorare.
        this.model = model;
        listView.setItems(model.getEmailList());
    }
        /* model.emailList.addListener((obs,oldEmail,newEmail)-> {
         if(newEmail == null)
         listView.setItems(null);
         else
         listView.setItems(newEmail);
        
         });
         */
    
//comm
    /*
     public void start() {
     Thread clientThread = new Thread(() -> {
     try {
     ServerSocket s = new ServerSocket(5000);

     while(true) {
     ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
     EmailManager e = (EmailManager) in.readObject(); // UPCAST perchè so che riceverò ogg EmailManager
     Email mail = e.getEmail();

     textArea2.setText(mail.getTesto());
     }
     } catch (Exception e) {
     e.printStackTrace();
     }
     });
     clientThread.start();
     }
     */

    @FXML
    private void modifyList(ActionEvent event) {
        Calendar cal = Calendar.getInstance();
        Email email = new Email(textFieldFrom.getText(), textFieldFrom.getText(), new ArrayList<String>() {
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
                Calendar cal = Calendar.getInstance(); //crea oggetto cal inizializzato all'ora e data corrente

                String mittente = textFieldTo.getText();

                ArrayList<String> destinatari = new ArrayList<>();
                destinatari.add(textFieldFrom.getText());

                String object = textFieldObject.getText();

                String text = textArea.getText();

                String time = cal.getTime().toString();

                Email email = new Email(textFieldFrom.getText(), mittente, destinatari, object, text, time);
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
    public void replyMsg(ActionEvent event) {

    }

     @FXML
    private void removeMsg(ActionEvent event) {
        Email item = listView.getSelectionModel().getSelectedItem();
        // listView.getItems().remove(item); //oggetto rimosso , solo da listview
        model.getEmailList().remove(item); //oggetto rimosso dal model->si propaga sulla listview
        EmailManager emailManager = new EmailManager(item, "REMOVE");
    }
}
