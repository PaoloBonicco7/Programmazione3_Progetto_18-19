package progettopaolofede;

import comunication.Email;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
}
