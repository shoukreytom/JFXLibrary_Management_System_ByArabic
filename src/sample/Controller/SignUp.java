package sample.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Model.DBConnection;
import sample.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SignUp {
    @FXML
    private TextField name;
    @FXML
    private PasswordField pass;
    @FXML
    private PasswordField conf_pass;
    @FXML
    private Button sign;

    @FXML
    public void initialize(){
        if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                (!pass.getText().isEmpty()&&!pass.getText().trim().isEmpty()) &&
                conf_pass.getText().equals(pass.getText())) {
            sign.setDisable(true);
        }else {
            sign.setDisable(false);
        }
    }
    @FXML
    public void signUp() throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection con = DBConnection.connection();
        Statement statement = con.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS user(name text, password text)");
        PreparedStatement pres = con.prepareStatement("INSERT INTO user VALUES(?,?)");
        pres.setString(1,name.getText());
        pres.setString(2,pass.getText());
        pres.execute();
        con.close();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/home.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }
}
