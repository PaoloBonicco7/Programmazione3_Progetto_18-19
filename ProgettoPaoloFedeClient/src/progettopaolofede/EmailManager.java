/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package progettopaolofede;

import java.io.Serializable;

/**
 *
 * @author gniammo
 */
//type of action:
//1.SEND
//2.REPLY
//3.REPLYALL
//4.REMOVE

public class EmailManager implements Serializable {
    private Email email;
    private String action;

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
    
    
}
