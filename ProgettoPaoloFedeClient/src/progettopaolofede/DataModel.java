package progettopaolofede;

<<<<<<< HEAD
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
=======
import comunication.Email;
>>>>>>> refs/remotes/origin/master
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

<<<<<<< HEAD
/**
 *
 * @author gniammo
 */
public class DataModel implements Serializable{

    private ObservableList<Email> emailList = FXCollections.observableArrayList();
    //oppure 
  //  private final ObservableList<Email> emailList = FXCollections.observableArrayList(email
  //          -> new Observable[]{email.IDProperty(), email.mittenteProperty(), email.destinatarioProperty(), email.argomentoProperty(), email.testoProperty(), email.dataProperty()});
/*
=======
import java.io.File;

public class DataModel {

    //private final ObservableList<Email> emailList = FXCollections.observableArrayList();

    private final ObservableList<Email> emailList = FXCollections.observableArrayList(email ->
            new Observable[]{email.IDProperty(),
                    email.mittenteProperty(),
                    email.destinatariProperty(),
                    email.argomentoProperty(),
                    email.testoProperty(),
                    email.dataProperty()
            });

>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
*/
    public ObservableList<Email> getEmailList() {
        return emailList;
    }
    public void modifyEmailList(Email e){
       // emailList.setAll(e);
        emailList.add(e);
    }
//ListProperty destinatario
    /*
     public void loadData(File file) { //per avere dei msg iniziali
     emailList.setAll(
     new Email(0, "mittente0", new ArrayList<String>() {
     {
     add("Destinatario0");
     }
     }, "argomento0", "testo0", "dataOggi"),
     new Email(1, "mittente1", new ArrayList<String>() {
     {
     add("Destinatario1");
     }
     }, "argomento1", "testo1", "dataOggi"),
     new Email(2, "mittente2", new ArrayList<String>() {
     {
     add("Destinatario2");
     }
     }, "argomento2", "testo2", "dataOggi"),
                
     new Email(0, "mittente0", new ArrayList<String>() {
     {
     add("Destinatario3");
     }
     }, "argomento3", "testo3", "dataOggi"));
        
                
                
     }
     */

    public void loadData() { //per avere dei msg iniziali
        emailList.setAll(
                new Email(0, "mittente0", new ArrayList<String>(){
                    {add("destinatario0");
                    }
                    }, "argomento0", "testo0", "dataOggi"),
                new Email(1, "mittente1", new ArrayList<String>(){
                    {add("destinatario1");
                    }
                    }, "argomento1", "testo1", "dataOggi"),
                new Email(2, "mittente2", new ArrayList<String>(){
                    {add("destinatario2");
                    }
                    }, "argomento2", "testo2", "dataOggi"),
                new Email(3, "mittente0", new ArrayList<String>(){
                    {add("destinatario3");
                    }
                    }, "argomento3", "testo3", "dataOggi"));
    }
 /* IDEA   
    public void caricoData(ArrayList<Email> array){
       for(Email e : array){
           emailList.setAll(e); //setAll non puo' essere usato così perchè ogni volta elimina la lita e gli passa come unico valore il parametro=> devo insere l'elenco dei parametri oppure creare ua collezione e passargliela come parametro.
       }
    }
 */
=======

    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    public void loadData(File file) { //per avere dei msg iniziali
        emailList.setAll(
                new Email(0, "mit0", "dest0", "arg0", "txt0", "data0"),
                new Email(1, "mit1", "dest1", "arg1", "txt1", "data1"),
                new Email(2, "mit2", "dest2", "arg2", "txt2", "data2")
        );
    }
>>>>>>> refs/remotes/origin/master
}
