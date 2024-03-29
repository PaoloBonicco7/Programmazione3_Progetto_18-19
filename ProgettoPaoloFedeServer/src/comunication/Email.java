package comunication;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable {

    private String ID;
    private String mittente;
    private ArrayList<String> destinatario;
    private String argomento;
    private String testo;
    private String data;

    private static final long serialVersionUID = 5950169519310163575L;

    public Email(String mittente, ArrayList<String> destinatario, String argomento, String testo, String data) {
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.argomento = argomento;
        this.testo = testo;
        this.data = data;
    }

    public Email() {
        this.ID = "";
        this.mittente = "";
        this.destinatario = new ArrayList<>();
        this.argomento = "";
        this.testo = "";
        this.data = "";
    }

    public String getID() {
        return ID;
    }

    public String getMittente() {
        return mittente;
    }

    public ArrayList<String> getDestinatario() {
        return destinatario;
    }

    public String getArgomento() {
        return argomento;
    }

    public String getTesto() {
        return testo;
    }

    public String getData() {
        return data;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public void setDestinatario(ArrayList<String> destinatario) {
        this.destinatario = destinatario;
    }

    public void setArgomento(String argomento) {
        this.argomento = argomento;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String toString() {
        //    return getID() + " " + getMittente() + " " + getDestinatario() + " " + getArgomento() + " " + getTesto() + "" + getData();
        return "Mittente: "+getMittente() + " Object: " + getArgomento() + " Text: " + getTesto() + " Date: " + getData()  + " Dest: " + getDestinatario() + " Id: " + getID();
    }
}

//CLASSE EMAIL CON PROPERTIES
//import java.util.ArrayList;
//import java.util.List;
//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.ListProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleListProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
/**
 *
 * @author gniammo
 */
/*
 public class Email implements Serializable {
    
 //ID
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
 //mittente
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

 //TODO -> convertire String in ListProperty
 //    
 ////destinatario
 //    private final ListProperty<String> destinatari = new SimpleListProperty<String>();
 //    
 //    public final ListProperty<String> destinatariProperty(){
 //        return this.destinatari;
 //    }
 //    public final List<String> getDestinatari(){
 //        return this.destinatariProperty().get();
 //    }
 //    public final void setDestinatari(ListProperty<String> destinatari){ //dubbio
 //        this.destinatariProperty().set(destinatari);
 //    }

    
 //destinatario STRING
    
 private final StringProperty destinatario = new SimpleStringProperty();

 public final StringProperty destinatarioProperty() {
 return this.destinatario;
 }

 public final String getDestinatario() {
 return this.mittenteProperty().get();
 }

 public final void setDestinatario(final String destinatario) {
 this.destinatarioProperty().set(destinatario);
 }
    
 //argomento
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
 //testo
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
 //data
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
 //costruttore  
 public Email(int ID, String mittente, String destinatario,String argomento, String testo, String data){
 setID(ID);
 setMittente(mittente);
 setDestinatario(destinatario);
 setArgomento(argomento);
 setTesto(testo);
 setData(data);
 }

 public String toString(){
 return getID()+" "+getMittente()+" "+ getDestinatario()+" "+getArgomento()+" "+getTesto()+""+getData();
 }
 }
 */
