package progettopaolofede;
<<<<<<< HEAD
//ss
=======
>>>>>>> refs/remotes/origin/master

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.layout.BorderPane;
=======
>>>>>>> refs/remotes/origin/master
import javafx.stage.Stage;

public class Client extends Application {

    @Override
<<<<<<< HEAD
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("list.fxml"));
        root.setLeft(listLoader.load());
        FXMLLoader sendLoader = new FXMLLoader(getClass().getResource("send.fxml"));
        root.setRight(sendLoader.load());
        ClientController clientController = sendLoader.getController();
        ListController listController = listLoader.getController();
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        DataModel model = new DataModel();
        clientController.initModel(model);
        listController.initModel(model);
        Scene scene = new Scene(root);
        //stage.setScene(scene);
        //stage.show();
        primaryStage.setScene(scene);
        primaryStage.show();
=======
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
>>>>>>> refs/remotes/origin/master
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
<<<<<<< HEAD
=======

>>>>>>> refs/remotes/origin/master
