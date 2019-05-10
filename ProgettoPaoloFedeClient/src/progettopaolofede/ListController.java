package progettopaolofede;

import comunication.Email;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 *
 * Il controller lega i dati mostrati nella GUI dalla lista ai dati presenti nel
 * model e assicura che l'oggetto currentEmail del model sia sempre l'oggetto
 * selezionato nella lista
 */
public class ListController {

    @FXML
    private ListView<Email> listView;
    @FXML
    private ListView<Email> testListView;

    private DataModel model;

    /*
     public void initModel(DataModel model) {
     // ensure model is only set once:
       
     if (this.model != null) {
     throw new IllegalStateException("Model can only be initialized once");
     }

     this.model = model;
     listView.setItems(model.getEmailList());

     listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)
     -> model.setCurrentEmail(newSelection));

     model.currentEmailProperty().addListener((obs, oldEmail, newEmail) -> {
     if (newEmail == null) {
     listView.getSelectionModel().clearSelection();
     } else {
     listView.getSelectionModel().select(newEmail);
     }
     });

     listView.setCellFactory(lv -> new ListCell<Email>() {
     @Override
     public void updateItem(Email email, boolean empty) {
     super.updateItem(email, empty);
     if (empty) {
     setText(null);
     } else { //VEDERE
     //   setText(email.getID() + " " +email.getDestinatari().toString() + "" + email.getMittente() + " " + email.getArgomento() + " " + email.getData() + " " + email.getTesto()); //ListProperty destinatari
     setText(email.getID() + " " +email.getDestinatario()+ "" + email.getMittente() + " " + email.getArgomento() + " " + email.getData() + " " + email.getTesto());
     System.out.println("TODO");
     } 
     }
     });
        
     }
     */
    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        model.loadData();// inizializzo il model con alcune email per poterci lavorare.
        this.model=model;
        listView.setItems(model.getEmailList());
        /* model.emailList.addListener((obs,oldEmail,newEmail)-> {
         if(newEmail == null)
         listView.setItems(null);
         else
         listView.setItems(newEmail);
        
         });
         */
    }

    @FXML
    private void testModel() {
        System.out.println("voglio vedere se il model è stato modificato\n");
        testListView.setItems(model.getEmailList());
    }
    
}
