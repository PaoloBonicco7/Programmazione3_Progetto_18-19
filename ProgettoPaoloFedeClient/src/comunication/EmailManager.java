package comunication;

import java.io.Serializable;

public class EmailManager implements Serializable {
    private Email email;
    private String action;
    private int port;
    private String utente;

    public EmailManager(Email email, String action) {
        this.email = email;
        this.action = action;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Email getEmail() {
        return email;
    }

    public String getAction() {
        return action;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public String toString(){
        return "";
    }
    public void setUtente(String utente){
        this.utente=utente;
    }
    public String getUtente(){
        return utente;
    }


}
