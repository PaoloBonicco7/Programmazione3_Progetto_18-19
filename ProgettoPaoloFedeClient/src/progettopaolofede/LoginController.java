/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package progettopaolofede;

import comunication.User;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;

/**
 *
 * @author gniammo
 */
public class LoginController {
    private DataModel model;
    
    @FXML
    ImageView image;
    @FXML
    ChoiceBox choiceBox;
    @FXML
    Button login;
    
    @FXML
    public void initialize(ArrayList<User> listaUtenti){
        ObservableList<String> list = FXCollections.observableArrayList();
        ArrayList<String> idUtente= new ArrayList<String>();
        for(User a : listaUtenti){
            idUtente.add(a.toString());
        }
        list.addAll(idUtente);
        choiceBox.setItems(list);
    }
    
    
    
    
    
    
}
