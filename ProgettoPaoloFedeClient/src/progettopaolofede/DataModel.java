package progettopaolofede;

import java.io.Serializable;
import java.util.ArrayList;

import comunication.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DataModel implements Serializable{

    private ObservableList<Email> emailList = FXCollections.observableArrayList();

    public ObservableList<Email> getEmailList() {
        return emailList;
    }

    public void addEmail(Email e){
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
