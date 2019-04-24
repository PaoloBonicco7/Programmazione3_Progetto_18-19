package comunication;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;

public class Email implements Serializable {

    //  TODO --> DESTINATARI è UNA STRINGA !!

    //  ID
    private final IntegerProperty ID = new SimpleIntegerProperty();
    //  MITTENTE
    private final StringProperty mittente = new SimpleStringProperty();
    //  DESTINATARI
    private final StringProperty destinatari = new SimpleStringProperty();
    //  ARGOMENTO
    private final StringProperty argomento = new SimpleStringProperty();
    //  TESTO
    private final StringProperty testo = new SimpleStringProperty();
    //  DATA
    private final StringProperty data = new SimpleStringProperty();

    public Email(int ID, String mittente, String destinatari, String argomento, String testo, String data) {
        setID(ID);
        setMittente(mittente);
        setDestinatari(destinatari);
        setArgomento(argomento);
        setTesto(testo);
        setData(data);
    }

    public final IntegerProperty IDProperty() {
        return this.ID;
    }

    public final int getID() {
        return this.IDProperty().get();
    }

    public final void setID(final int ID) {
        this.IDProperty().set(ID);
    }

    public final StringProperty mittenteProperty() {
        return this.mittente;
    }

    public final String getMittente() {
        return this.mittenteProperty().get();
    }

    public final void setMittente(final String mittente) {
        this.mittenteProperty().set(mittente);
    }

    public final StringProperty destinatariProperty() {
        return this.destinatari;
    }

    public final String getDestinatari() {
        return this.destinatariProperty().get();
    }

    public final void setDestinatari(final String destinatari) {
        this.mittenteProperty().set(destinatari);
    }

    public final StringProperty argomentoProperty() {
        return this.argomento;
    }

    public final String getArgomento() {
        return this.argomentoProperty().get();
    }

    public final void setArgomento(final String argomento) {
        this.argomentoProperty().set(argomento);
    }

    public final StringProperty testoProperty() {
        return this.testo;
    }

    public final String getTesto() {
        return this.testoProperty().get();
    }

    public final void setTesto(final String testo) {
        this.testoProperty().set(testo);
    }

    public final StringProperty dataProperty() {
        return this.data;
    }

    public final String getData() {
        return this.dataProperty().get();
    }

    public final void setData(final String data) {
        this.dataProperty().set(data);
    }
}
