package comunication;

import java.io.Serializable;

public class User implements Serializable{

    private String id;
    int port;
    
    public User(String id, int port) {
        this.id = id;
        this.port=port;
    }

    public String getId() {
        return id;
    }
    public int getPort(){
        return port;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString(){
        return id;
    }
    
}
