package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("update.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Truck Journal");
        primaryStage.setScene(new Scene(root, 574, 670));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}