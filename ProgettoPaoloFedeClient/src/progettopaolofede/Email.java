package progettopaolofede;

import javafx.beans.property.*;

import java.io.Serializable;
import java.util.List;

public class Email implements Serializable {

    private final IntegerProperty ID = new SimpleIntegerProperty();

    public final IntegerProperty IDProperty() {
        return this.ID;
    }

    public final int getID() {
        return this.IDProperty().get();
    }

    public final void setID(final int ID) {
        this.IDProperty().set(ID);
    }


    private final StringProperty mittente = new SimpleStringProperty();

    public final StringProperty mittenteProperty() {
        return this.mittente;
    }

    public final String getMittente() {
        return this.mittenteProperty().get();
    }

    public final void setMittente(final String mittente) {
        this.mittenteProperty().set(mittente);
    }


    private final ListProperty<String> destinatari = new SimpleListProperty<String>();
    
    public final ListProperty<String> destinatariProperty(){
        return this.destinatari;
    }

    public final List<String> getDestinatari(){
        return this.destinatariProperty().get();
    }

    public final void setDestinatari(ListProperty<String> destinatari){ //dubbio
        this.destinatariProperty().set(destinatari);
    }


    private final StringProperty argomento = new SimpleStringProperty();

    public final StringProperty argomentoProperty() {
        return this.argomento;
    }

    public final String getArgomento() {
        return this.argomentoProperty().get();
    }

    public final void setArgomento(final String argomento) {
        this.argomentoProperty().set(argomento);
    }


    private final StringProperty testo = new SimpleStringProperty();

    public final StringProperty testoProperty() {
        return this.testo;
    }

    public final String getTesto() {
        return this.testoProperty().get();
    }

    public final void setTesto(final String testo) {
        this.testoProperty().set(testo);
    }


    private final StringProperty data = new SimpleStringProperty();

    public final StringProperty dataProperty() {
        return this.data;
    }

    public final String getData() {
        return this.dataProperty().get();
    }

    public final void setData(final String data) {
        this.dataProperty().set(data);
    }


    public Email(int ID, ListProperty<String> destinatari, String argomento, String testo, String data) {
        setID(ID);
        setDestinatari(destinatari);
        setArgomento(argomento);
        setTesto(testo);
        setData(data);
    }
}
