package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public static Stage stage;
    public String stutus = "";
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/sample/FXML/home.fxml"));
        primaryStage.setScene(new Scene(root));
//        if(stutus.equals("old")) {
//            Parent root = FXMLLoader.load(getClass().getResource("/sample/FXML/user_login.fxml"));
//            primaryStage.setTitle("Library Assistance");
//            primaryStage.setScene(new Scene(root));
//        }else {
//            Parent root = FXMLLoader.load(getClass().getResource("/sample/FXML/user_signup.fxml"));
//            primaryStage.setTitle("Library Assistance");
//            primaryStage.setScene(new Scene(root));
//        }
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        File file = new File("users.db");
        if(!file.exists()) {
            stutus = "new";
        }else {
            stutus = "old";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
