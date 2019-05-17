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
        emailList.setAll();
    }

}
