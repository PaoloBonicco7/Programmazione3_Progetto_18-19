/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package progettopaolofede;


import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

/**
 *
 * @author gniammo
 */

 /**
 * 
 * Il controller lega i dati mostrati nella GUI dalla lista ai dati presenti nel model
 * e assicura che l'oggetto currentEmail del model sia sempre l'oggetto selezionato nella lista
 */
public class ListController {
    

    @FXML
    private ListView<Email> listView;

    private DataModel model;

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
}
