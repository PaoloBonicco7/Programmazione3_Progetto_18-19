/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package comunication;

import java.io.Serializable;

/**
 *
 * @author gniammo
 */

//tipi di azione:
/*
*WRITE = il server quando riceve scrive su json e poi va informato il client destinatario del msg (observable?)
*WRITEALL= direi che possiamo usare solo write e aggiornare piu' client, no?
*REMOVE = il server riceve l'email, la cercanel json e la rimuove
*REPLY = viene creato un oggetto email copiandolo da quelli che vede il cliente e lo spedice al mittente , server fa come write
*REPLY ALL = come reply ma a tutti
*/


public class EmailHandler implements Serializable {
    private Email email;
    private String action;
    
    public EmailHandler(Email email,String action) {
        this.email = email;
        this.action = action;
    }

   public Email getEmail(){
       return email;
   }
   public String getAction(){
       return action;
   }
   
    
    
}
