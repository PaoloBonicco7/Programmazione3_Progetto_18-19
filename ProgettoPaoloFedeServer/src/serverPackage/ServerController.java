package serverPackage;

import comunication.EmailHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServerController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private TextArea textArea;
    @FXML
    private Button accept;
    @FXML
    private Button bottoneScrivi; //bottone che se cliccato il server scrive su json
    @FXML
    private Button bottoneLeggi; //bottone che se cliccato fa leggere al server il file json

    @FXML
    private void handleButtonAction(ActionEvent event) { //bottone inutile
        System.out.println("DO NOTHING!");
        label.setText("DO NOTHING");
    }

    @FXML
    private void handleConnection() { //handle del bottone connetti -> se clicchi si mette in attesa di ricevere connessione. -> VA TOLTO E GESTITO IN ALTRO MODO CONNESSIONE con THREADPOOL
        Runnable run = new Runnable() {
            @Override
            public void run() {
                ServerSocket s = null;
                try {
                    s = new ServerSocket(5000);
                    while (true){
                        Socket incoming = s.accept(); // si mette in attesa di richiesta di connessione e la apre
                        ObjectInputStream in = new ObjectInputStream(incoming.getInputStream());
                        EmailHandler e = (EmailHandler)in.readObject(); // UPCAST perchè so che riceverò ogg Email
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                //CODICE PER LEGGERE MSG INVIATI
                                FileEditor.newFile();
                                if(e != null){
                                    FileEditor.saveToJson(e.getEmail());
                                }
                                textArea.setText(e.getEmail().toString()+e.getAction());
         
                                /*
                                Qua vado a leggere cosa mi ha inviato e in base al msg ricevuto il server decide un po
                                quale metodo inviare.
                                tipo.
                                if(e.action.getAction().equals("WRITE"))
                                    metodoControllerWrite(); 
                                elseif(e.action.getAction().equals("REMOVE"))
                                    metodoControllerRemove(); ecc
                                ...
                                
                                
                                */
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(run).start(); //??

        /*
       String nomeAccount = "";
       InputStream inStream = incoming.getInputStream();
       Scanner in = new Scanner(inStream);
       nomeAccount = in.nextLine(); //ricevo il nome
       */
    }
    /*
    private void writeHandler(){
        ArrayList<Email> list = new ArrayList<>();
        list.add(e);
        saveToJson();
    }
    */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }

    /*
    @FXML
    private void writeToJsn() throws IOException { //EMAIL deve essere passata come parametro.
        ArrayList<String> destProva = new ArrayList<String>();
        destProva.add("primoDestinatario");
        destProva.add("secondoDestinatario");
        Email email = new Email(3, "ciao", destProva, "ciao", "ciao", "ciao");
        Gson gson = new Gson();
        String jsonString = gson.toJson(email);
        FileWriter file = new FileWriter("C:\\Users\\paolo\\Desktop\\CasellaPostale"); //scrittura in append true
        try {
            file.write(jsonString + "\n"); //scrive il JSON su file txt
            label.setText("Scrittura su json completa");

        } catch (IOException e) {
            System.out.println("ERRORE SCRITTURA su txt");
            e.getMessage();
        } finally {
            //file.flush();
            file.close();
            //non mettere flush che da eccezione

        }

    }

    @FXML
    private void readToJsn() throws FileNotFoundException {

        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream("/home/gniammo/Scrivania/casellaPostale")));
            JsonParser jsonParser = new JsonParser();
            Gson myGson = new Gson();
            JsonArray userarray = jsonParser.parse(reader).getAsJsonArray(); //??
            List listEmail = new ArrayList<>();
            for (JsonElement aUser : userarray) {
                Email email = myGson.fromJson(aUser, Email.class);
                textArea.setText(email.toString());
                listEmail.add(email);
            }
           

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
