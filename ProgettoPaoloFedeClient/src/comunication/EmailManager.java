package comunication;

import java.io.Serializable;

//type of action:
    //1.SEND
    //2.REPLY
    //3.REPLYALL
    //4.REMOVE

public class EmailManager implements Serializable {
    private Email email;
    private String action;

    private static final long serialVersionUID = 5950169519310163575L;

    public EmailManager(Email email, String action) {
        this.email = email;
        this.action = action;
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

}
