package progettopaolofede;

import comunication.Email;
import comunication.EmailHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
