package progettopaolofede;

import java.io.Serializable;
import java.util.ArrayList;

import comunication.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DataModel implements Serializable{

    private ObservableList<Email> emailList = FXCollections.observableArrayList();
    //oppure 
  //  private final ObservableList<Email> emailList = FXCollections.observableArrayList(email
  //          -> new Observable[]{email.IDProperty(), email.mittenteProperty(), email.destinatarioProperty(), email.argomentoProperty(), email.testoProperty(), email.dataProperty()});
/*
    private final ObjectProperty<Email> currentEmail = new SimpleObjectProperty<>(null);

    public ObjectProperty<Email> currentEmailProperty() {
        return currentEmail;
    }

    public final Email getCurrentEmail() {
        return currentEmailProperty().get();
    }

    public final void setCurrentEmail(Email email) {
        currentEmailProperty().set(email);
    }
*/

    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    public void modifyEmailList(Email e){
       // emailList.setAll(e);
        emailList.add(e);
    }


    public void loadData() { //per avere dei msg iniziali
        emailList.setAll(
                new Email("Paolo", "mittente0", new ArrayList<String>(){
                    {add("destinatario0");
                    }
                    }, "argomento0", "testo0", "dataOggi"),
                new Email("Federico", "mittente1", new ArrayList<String>(){
                    {add("destinatario1");
                    }
                    }, "argomento1", "testo1", "dataOggi"),
                new Email("Felice", "mittente2", new ArrayList<String>(){
                    {add("destinatario2");
                    }
                    }, "argomento2", "testo2", "dataOggi"));
    }

}
