package progettopaolofede;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        FXMLLoader listLoader = new FXMLLoader(getClass().getResource("list.fxml"));
        root.setCenter(listLoader.load());
        ListController listController = listLoader.getController();
        //  Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        DataModel model = new DataModel();
        listController.initModel(model);
        Scene scene = new Scene(root);
        //stage.setScene(scene);
        //stage.show();
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

