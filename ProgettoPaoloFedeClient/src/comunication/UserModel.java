package comunication;

import java.util.ArrayList;

public class UserModel { //inizializzo 3 utenti

    ArrayList<User> listaUtenti = new ArrayList<User>();
    
    public UserModel(){
        listaUtenti.add(new User("paolo@gmail.com"));
        listaUtenti.add(new User("federico@gmail.com"));
        listaUtenti.add(new User("andrea@gmail.com"));
    }

    public ArrayList<User> getListaUtenti(){
        return listaUtenti;
    }
}
