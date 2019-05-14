package progettopaolofede;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("list.fxml"));
        root.setLeft(listLoader.load());
        FXMLLoader sendLoader = new FXMLLoader(getClass().getResource("send.fxml"));
        root.setRight(sendLoader.load());
        ClientController clientController = sendLoader.getController();
        ListController listController = listLoader.getController();
        DataModel model = new DataModel();
        clientController.initModel(model);
        //clientController.start();   // THREAD CHE SI METTE IN ATTESA DI RICEVERE MAIL DAL SERVER
        listController.initModel(model);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
