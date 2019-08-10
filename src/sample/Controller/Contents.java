package sample.Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sample.Main;
import sample.Model.BooksModel;
import sample.Model.ContentsModel;

import java.sql.*;
import java.time.LocalDate;

public class Contents {

    Stage add_stage = new Stage();
    Stage edit_stage = new Stage();
    @FXML
    private TableView<ContentsModel> table;
    @FXML
    private TableColumn<ContentsModel,Integer> id;
    @FXML
    private TableColumn<ContentsModel,String> name;
    @FXML
    private TableColumn<ContentsModel,String> mount;
    @FXML
    private TableColumn<ContentsModel,String> date;
    @FXML
    private TableColumn<ContentsModel,Integer> num;
    private ObservableList<ContentsModel> data;

    @FXML
    public void initialize() {
        loadData();
    }

    public void addContent() {
        AddContent addBook = new AddContent();
        add_stage.setScene(new Scene(addBook));
        add_stage.initOwner(Main.stage);
        add_stage.show();
    }

    @FXML
    void loadData(){

        try(Connection con = DriverManager.getConnection("jdbc:sqlite:contents.db")) {
            data = FXCollections.observableArrayList();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM data");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new ContentsModel(rs.getInt("id"),rs.getString("date"),rs.getString("name"),rs.getInt("number"),
                        rs.getString("mount")));
            }
            id.setCellValueFactory(new PropertyValueFactory<ContentsModel,Integer>("id"));
            name.setCellValueFactory(new PropertyValueFactory<ContentsModel,String>("name"));
            mount.setCellValueFactory(new PropertyValueFactory<ContentsModel,String>("mount"));
            date.setCellValueFactory(new PropertyValueFactory<ContentsModel,String>("date"));
            num.setCellValueFactory(new PropertyValueFactory<ContentsModel,Integer>("num"));

            table.setItems(data);
        }catch (SQLException e) {
            System.out.println("Can't load data");
        }
    }

    @FXML
    public void edit() {
        if(table.getSelectionModel().getSelectedItem() != null) {
            int id = table.getSelectionModel().getSelectedItem().getId();
            String tName = table.getSelectionModel().getSelectedItem().getName();
            String tMount = table.getSelectionModel().getSelectedItem().getMount();
            int tNum = table.getSelectionModel().getSelectedItem().getNum();
            EditContent edit = new EditContent(id,tName,tMount,tNum);
            edit_stage.setScene(new Scene(edit));
            edit_stage.initOwner(Main.stage);
            edit_stage.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("تعديل");
            alert.setContentText("اختار العنصر الذي تريد تعديله اولا");
            alert.setWidth(alert.getContentText().length());
            alert.setHeaderText("");
            alert.show();
        }
    }

    @FXML
    public void delete() {
        if(table.getSelectionModel().getSelectedItem() != null) {
            try(Connection con = DriverManager.getConnection("jdbc:sqlite:contents.db")) {
                PreparedStatement ps = con.prepareStatement("delete from data where id = ?");
                int id = table.getSelectionModel().getSelectedItem().getId();
                ps.setInt(1, id);
                ps.execute();
                loadData();
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("حذف");
            alert.setContentText("اختار العنصر الذي تريد حذفه اولا");
            alert.setWidth(alert.getContentText().length());
            alert.setHeaderText("");
            alert.show();
        }
    }

    @FXML
    public void home() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/home.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }
}


////////////////////////////////////////////AddBook Class///////////////////////////////////////////////////////////
class AddContent extends VBox {

    //////////////Constructor/////////////////////
    public AddContent() {
        drawScreen();
    }
    /////////////////////////////////////////////


    private void drawScreen() {
        //Labels
        Label error = new Label();
        Label lName = new Label("اسم المحتوي");
        Label lMount = new Label("نوع الكمية");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField();
        TextField mount = new TextField();
        TextField num = new TextField();

        //Buttons
        Button submit = new Button("اضافة");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(error);
        HBox hName = new HBox(name,lName);
        HBox hMount = new HBox(mount,lMount);
        HBox hNum = new HBox(num,lNum);
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        error.setTextFill(Color.RED);
        error.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lMount.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم المحتوي");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                mount.requestFocus();
            }else {
                error.setText("");
            }
        });
        mount.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        mount.setAlignment(Pos.CENTER_RIGHT);
        mount.setPromptText("ادخل نوع الكمية");
        mount.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                error.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل العدد");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                if(event.getCode().equals(KeyCode.ENTER)) {
                    if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                            (!mount.getText().isEmpty()&&!mount.getText().trim().isEmpty()) &&
                            (!num.getText().isEmpty()&&!num.getText().trim().isEmpty())) {
                        if(!num.getText().matches("\\d*")) {
                            error.setText("ادخل رقما في حقل العدد");
                        }else {
                            try(Connection con = DriverManager.getConnection("jdbc:sqlite:contents.db")) {
                                Statement statement = con.createStatement();
                                statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,date TEXT," +
                                        "name TEXT,mount TEXT,number INTEGER)");
                                PreparedStatement ps = con.prepareStatement("INSERT INTO data(date,name,mount,number" +
                                        ") VALUES(?,?,?,?)");
                                ps.setString(1, LocalDate.now().getYear()+"/"+LocalDate.now().getMonthValue()+"/"+LocalDate.now().getDayOfMonth());
                                ps.setString(2,name.getText());
                                ps.setString(3,mount.getText());
                                ps.setInt(4,Integer.parseInt(num.getText()));

                                ps.execute();
                                name.clear();
                                mount.clear();
                                num.clear();
                                error.setTextFill(Color.GREEN);
                                error.setText("تم اضافة المحتوي بنجاح");
                                name.requestFocus();
                            }catch (SQLException e) {
                                error.setTextFill(Color.RED);
                                System.out.println(e.getMessage());
                                error.setText("عفوا حدث خطأ ما");
                            }
                        }
                    }else {
                        error.setTextFill(Color.RED);
                        error.setText("لايمكنك ترك الحقول فارغا");
                    }
                }else {
                    error.setText("");
                }
            }else {
                error.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!mount.getText().isEmpty()&&!mount.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*")) {
                    error.setText("ادخل رقما في حقل العدد");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:contents.db")) {
                        Statement statement = con.createStatement();
                        statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,date TEXT," +
                                "name TEXT,mount TEXT,number INTEGER)");
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(date,name,mount,number" +
                                ") VALUES(?,?,?,?)");
                        ps.setString(1, LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue()+"-"+LocalDate.now().getDayOfMonth());
                        ps.setString(2,name.getText());
                        ps.setString(3,mount.getText());
                        ps.setInt(4,Integer.parseInt(num.getText()));

                        ps.execute();
                        name.clear();
                        mount.clear();
                        num.clear();
                        error.setTextFill(Color.GREEN);
                        error.setText("تم اضافة المحتوي بنجاح");
                        name.requestFocus();
                    }catch (SQLException e) {
                        error.setTextFill(Color.RED);
                        System.out.println(e.getMessage());
                        error.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                error.setTextFill(Color.RED);
                error.setText("لايمكنك ترك الحقول فارغا");
            }
        });
        cancel.setOnAction(event -> {
            new Books().add_stage.close();
        });

        //HBoxes's properties
        herror.setAlignment(Pos.CENTER);
        hName.setSpacing(10.0);
        hName.setPrefWidth(200.0);
        hName.setAlignment(Pos.CENTER);
        hMount.setSpacing(10.0);
        hMount.prefWidth(200.0);
        hMount.setAlignment(Pos.CENTER);
        hNum.setSpacing(10.0);
        hNum.prefWidth(200.0);
        hNum.setAlignment(Pos.CENTER);
        hControle.setAlignment(Pos.CENTER);
        hControle.setSpacing(40.0);

        //Base Properties
        getChildren().addAll(hName,hMount,hNum,herror,hControle);
        setSpacing(15.0);
        setPadding(new Insets(40,0,0,0));
    }
}

/////////////////////////////Edit Class/////////////////////////////////////////////////////
class EditContent extends VBox {

    //////////////Constructor/////////////////////
    public EditContent(int id,String tName,String tMount,int tNum) {
        drawScreen(id,tName,tMount,tNum);
    }
    /////////////////////////////////////////////


    private void drawScreen(int id,String tName,String tMount,int tNum) {
        //Labels
        Label error = new Label();
        Label lName = new Label("اسم المحتوي");
        Label lMount = new Label ("نوع الكمية");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField(tName);
        TextField mount = new TextField(tMount);
        TextField num = new TextField(tNum+"");

        //Buttons
        Button submit = new Button("تعديل");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(error);
        HBox hName = new HBox(name,lName);
        HBox hStage = new HBox(mount,lMount);
        HBox hNum = new HBox(num,lNum);
        HBox hPrice = new HBox();
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        error.setTextFill(Color.RED);
        error.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lMount.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم الكتاب");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                mount.requestFocus();
            }else {
                error.setText("");
            }
        });
        mount.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        mount.setAlignment(Pos.CENTER_RIGHT);
        mount.setPromptText("ادخل المرحلة اساس ام ثانوى");
        mount.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                error.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل عدد الكتب من هذا الصنف");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {

            }else {
                error.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!mount.getText().isEmpty()&&!mount.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*")) {
                    error.setText("ادخل رقما في حقلي العدد و السعر");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
                        PreparedStatement ps = con.prepareStatement("UPDATE data SET name=?,mount=?,number=?" +
                                "WHERE id=?");
                        ps.setString(1,name.getText());
                        ps.setString(2,mount.getText());
                        ps.setInt(3,Integer.parseInt(num.getText()));
                        ps.setInt(4,id);
                        ps.execute();
                        name.clear();
                        mount.clear();
                        num.clear();
                        error.setText("تم تعديل تفاصيل الكتاب بنجاح");
                    }catch (SQLException e) {
                        System.out.println(e.getMessage());
                        error.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                error.setText("لايمكنك ترك الحقول فارغا");
            }
        });
        cancel.setOnAction(event -> new Books().edit_stage.close());

        //HBoxes's properties
        herror.setAlignment(Pos.CENTER);
        hName.setSpacing(10.0);
        hName.setPrefWidth(200.0);
        hName.setAlignment(Pos.CENTER);
        hStage.setSpacing(10.0);
        hStage.prefWidth(200.0);
        hStage.setAlignment(Pos.CENTER);
        hNum.setSpacing(10.0);
        hNum.prefWidth(200.0);
        hNum.setAlignment(Pos.CENTER);
        hPrice.setSpacing(10.0);
        hPrice.setPrefWidth(200.0);
        hPrice.setAlignment(Pos.CENTER);
        hControle.setAlignment(Pos.CENTER);
        hControle.setSpacing(40.0);

        //Base Properties
        getChildren().addAll(hName,hStage,hNum,hPrice,herror,hControle);
        setSpacing(15.0);
        setPrefWidth(500);
        setPrefHeight(450);
        setPadding(new Insets(40,0,0,0));
    }
}