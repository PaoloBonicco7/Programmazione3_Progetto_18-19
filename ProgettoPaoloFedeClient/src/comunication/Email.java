package comunication;

import java.io.Serializable;
import java.util.ArrayList;

public class Email implements Serializable{
    private int ID;
    private String mittente;
    private ArrayList<String> destinatario;
    private String argomento;
    private String testo;
    private String data;

    public Email(int ID, String mittente, ArrayList<String> destinatario, String argomento, String testo, String data) {
        this.ID = ID;
        this.mittente = mittente;
        this.destinatario = destinatario;
        this.argomento = argomento;
        this.testo = testo;
        this.data = data;
    }

    @Override
    public String toString(){
        return "id:"+ID+"\nmittente: "+mittente+"\ndestinatario: "+destinatario+"\nargomento: "
                +argomento+"\ntesto: "+testo+"\ndata "+data;

    }

    public int getID() {
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

    public void setID(int ID) {
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
}
