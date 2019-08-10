package sample.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Model.DBConnection;
import sample.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LogIn {

    @FXML
    private TextField name;
    @FXML
    private PasswordField pass;
    @FXML
    private Button log;
    @FXML
    private Button forget;
    @FXML
    private Label message;
    @FXML
    private MenuItem co;
    @FXML
    private MenuItem cu;
    @FXML
    private MenuItem pa;
    @FXML
    private MenuItem se;

    @FXML
    public void initialize() {
        forget.setCursor(Cursor.HAND);
        new FontAwesomeIconView().getDefaultGlyph();
        co.setOnAction(e -> {
            name.copy();
        });
        cu.setOnAction(event -> {
            name.cut();
        });
        pa.setOnAction(event -> {
            name.paste();
        });
        se.setOnAction(event -> {
            name.selectAll();
        });
    }

    @FXML
    public void contex_requested() {
        if(!name.getText().isEmpty() && !name.getText().trim().isEmpty()) {
            co.setDisable(false);
            cu.setDisable(false);
            se.setDisable(false);
        }
    }
    @FXML
    public void logIn() throws Exception{
        Connection con = DBConnection.connection();
        PreparedStatement pres = con.prepareStatement("SELECT * FROM user");
        ResultSet res = pres.executeQuery();
        String name = "";
        String pass = "";
        while (res.next()) {
            name = res.getString("name");
            pass = res.getString("password");
        }

        if(this.name.getText().equals(name) && this.pass.getText().equals(pass)) {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/sample/FXML/home.fxml"));
            Parent root = loader.load();
            Main.stage.setScene(new Scene(root));
        }
        con.close();
    }

    @FXML
    public void forget_pass() throws Exception{
        message.setText("أدخل اسم المستخدم");
        pass.setDisable(true);
        name.requestFocus();
        if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty())) {
            Connection con = DBConnection.connection();
            PreparedStatement pres = con.prepareStatement("SELECT * from user");
            ResultSet res = pres.executeQuery();
            while (res.next()) {
                String name = res.getString("name");
                if(name.equals(this.name.getText())) {
                    String pass = res.getString("password");
                    message.setText(pass);
                    break;
                }
            }
        }
    }
}
