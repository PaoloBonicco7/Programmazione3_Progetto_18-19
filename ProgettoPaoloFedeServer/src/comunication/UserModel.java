package comunication;

import java.io.Serializable;
import java.util.ArrayList;

public class UserModel implements Serializable{ //inizializzo 3 utenti

    private int port1 = 6000;
    private int port2 = 6001;
    private int port3 = 6002;

    ArrayList<User> listaUtenti = new ArrayList<User>();

    public UserModel() {
        listaUtenti.add(new User("paolo@gmail.com", port1));
        listaUtenti.add(new User("federico@gmail.com", port2));
        listaUtenti.add(new User("andrea@gmail.com", port3));
    }

    public ArrayList<User> getListaUtenti() {
        return listaUtenti;
    }
}
