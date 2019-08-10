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
import javafx.util.Callback;
import javafx.util.Pair;
import sample.Main;
import sample.Model.BooksModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Books {

    Stage add_stage = new Stage();
    Stage edit_stage = new Stage();
    @FXML
    private ComboBox<Pair<String,String>> book_choice;
    @FXML
    private TableView<BooksModel> table;
    @FXML
    private TableColumn<BooksModel,Integer> id;
    @FXML
    private TableColumn<BooksModel,String> name;
    @FXML
    private TableColumn<BooksModel,String> t_class;
    @FXML
    private TableColumn<BooksModel,String> stage;
    @FXML
    private TableColumn<BooksModel,Integer> num;
    @FXML
    private TableColumn<BooksModel,Integer> price;
    private ObservableList<BooksModel> data;
    private boolean is_deleted = false;

    @FXML
    public void initialize() {
        List<Pair<String,String>> list = new ArrayList<>();
        list.add(new Pair<>("1","كتب مدرسية"));
        list.add(new Pair<>("2","كتب اخرى"));
        Callback<ListView<Pair<String,String>>,ListCell<Pair<String,String>>> fac  = (v1) -> new ListCell<Pair<String,String>>() {

            @Override
            protected void updateItem(Pair<String,String> item,boolean isEmpty) {
                super.updateItem(item,isEmpty);
                if(isEmpty)
                    setText("");
                else
                    setText(item.getValue());
            }
        };
        book_choice.setCellFactory(fac);
        book_choice.setButtonCell(fac.call(null));
        book_choice.getItems().addAll(list);
        book_choice.setValue(list.get(0));
        book_choice.setOnAction(event -> {
            if(book_choice.getValue().getValue().equalsIgnoreCase("كتب اخرى")) {
                otherBooks();
            }else {
                loadData();
            }
        });
        loadData();
    }

    public void addBook() {
        if(!book_choice.getValue().getValue().equalsIgnoreCase("كتب اخرى")) {
            AddBook addBook = new AddBook();
            add_stage.setScene(new Scene(addBook));
        }else {
            AddBook addBook = new AddBook("other books");
            add_stage.setScene(new Scene(addBook));
        }
        add_stage.show();
        add_stage.setOnCloseRequest(event -> {
        });
    }

    @FXML
    public void loadData(){

        if(is_deleted) {
            table.getColumns().add(2,t_class);
            table.getColumns().add(3,stage);
            is_deleted = false;
        }
        try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
            data = FXCollections.observableArrayList();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM data");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new BooksModel(rs.getInt("id"),rs.getString("name"),rs.getString("class"),
                        rs.getString("stage"),rs.getInt("number"),rs.getInt("price")));
            }
            id.setCellValueFactory(new PropertyValueFactory<BooksModel,Integer>("id"));
            name.setCellValueFactory(new PropertyValueFactory<BooksModel,String>("name"));
            t_class.setCellValueFactory(new PropertyValueFactory<BooksModel,String>("clas"));
            stage.setCellValueFactory(new PropertyValueFactory<BooksModel,String>("stage"));
            num.setCellValueFactory(new PropertyValueFactory<BooksModel,Integer>("num"));
            price.setCellValueFactory(new PropertyValueFactory<BooksModel,Integer>("price"));

            table.setItems(data);
        }catch (SQLException e) {
            System.out.println("Can't load data");
        }
    }

    @FXML
    public void edit() {
        if(!book_choice.getValue().getValue().equalsIgnoreCase("كتب اخرى")) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                int id = table.getSelectionModel().getSelectedItem().getId();
                String tName = table.getSelectionModel().getSelectedItem().getName();
                String tClass = table.getSelectionModel().getSelectedItem().getClas();
                String tStage = table.getSelectionModel().getSelectedItem().getStage();
                int tNum = table.getSelectionModel().getSelectedItem().getNum();
                int tPrice = table.getSelectionModel().getSelectedItem().getPrice();
                Edit edit = new Edit(id, tName, tClass, tStage, tNum, tPrice);
                edit_stage.setScene(new Scene(edit));
                edit_stage.initOwner(Main.stage);
                edit_stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("تعديل");
                alert.setContentText("اختار العنصر الذي تريد تعديله اولا");
                alert.setWidth(alert.getContentText().length());
                alert.setHeaderText("");
                alert.show();
            }
        }else {
            if (table.getSelectionModel().getSelectedItem() != null) {
                int id = table.getSelectionModel().getSelectedItem().getId();
                String tName = table.getSelectionModel().getSelectedItem().getName();
                int tNum = table.getSelectionModel().getSelectedItem().getNum();
                int tPrice = table.getSelectionModel().getSelectedItem().getPrice();
                Edit edit = new Edit(id, tName, tNum, tPrice);
                edit_stage.setScene(new Scene(edit));
                edit_stage.initOwner(Main.stage);
                edit_stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("تعديل");
                alert.setContentText("اختار العنصر الذي تريد تعديله اولا");
                alert.setWidth(alert.getContentText().length());
                alert.setHeaderText("");
                alert.show();
            }
        }
    }

    @FXML
    public void delete() {
        if(!book_choice.getValue().getValue().equalsIgnoreCase("كتب اخرى")) {
            if (table.getSelectionModel().getSelectedItem() != null) {
                try (Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
                    PreparedStatement ps = con.prepareStatement("delete from data where id = ?");
                    int id = table.getSelectionModel().getSelectedItem().getId();
                    ps.setInt(1, id);
                    ps.execute();
                    loadData();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("حذف");
                alert.setContentText("اختار العنصر الذي تريد حذفه اولا");
                alert.setWidth(alert.getContentText().length());
                alert.setHeaderText("");
                alert.show();
            }
        }else {
            if (table.getSelectionModel().getSelectedItem() != null) {
                try (Connection con = DriverManager.getConnection("jdbc:sqlite:otherBooks.db")) {
                    PreparedStatement ps = con.prepareStatement("delete from data where id = ?");
                    int id = table.getSelectionModel().getSelectedItem().getId();
                    ps.setInt(1, id);
                    ps.execute();
                    otherBooks();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("حذف");
                alert.setContentText("اختار العنصر الذي تريد حذفه اولا");
                alert.setWidth(alert.getContentText().length());
                alert.setHeaderText("");
                alert.show();
            }
        }
    }

    @FXML
    public void home() throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/home.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }

    private void otherBooks() {
        table.getColumns().remove(this.t_class);
        table.getColumns().remove(this.stage);
        table.resizeColumn(this.id,30.0);
        data.clear();
        try(Connection con = DriverManager.getConnection("jdbc:sqlite:otherBooks.db")) {
            Statement statement = con.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,number INTEGER,price INTEGER,date TEXT)");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM data");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new BooksModel(rs.getInt("id"),rs.getString("name"),rs.getInt("number"),rs.getInt("price")));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        table.setItems(data);
        is_deleted = true;
    }

    @FXML
    public void refreshTable() {
        if(!book_choice.getValue().getValue().equalsIgnoreCase("كتب اخرى")) {
            loadData();
        }else {
            otherBooks();
        }
    }
}


////////////////////////////////////////////AddBook Class///////////////////////////////////////////////////////////
class AddBook extends VBox {

    //////////////Constructor/////////////////////
    public AddBook() {
        drawScreen();
    }
    /////////////////////////////////////////////

    //////////////Constructor/////////////////////
    public AddBook(String name) {
        drawScreen(name);
    }
    /////////////////////////////////////////////


    private void drawScreen() {
        //Labels
        Label message = new Label();
        Label lName = new Label("اسم الكتاب");
        Label lClass = new Label("الصف");
        Label lPrice = new Label("السعر");
        Label lStage = new Label ("المرحلة");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField();
        TextField price = new TextField();
        TextField clas = new TextField();
        TextField stage = new TextField();
        TextField num = new TextField();

        //Buttons
        Button submit = new Button("اضافة");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(message);
        HBox hName = new HBox(name,lName);
        HBox hClass = new HBox(clas,lClass);
        HBox hStage = new HBox(stage,lStage);
        HBox hNum = new HBox(num,lNum);
        HBox hPrice = new HBox(price,lPrice);
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        message.setTextFill(Color.RED);
        message.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lClass.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lStage.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lPrice.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم الكتاب");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                clas.requestFocus();
            }else {
                message.setText("");
            }
        });
        clas.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        clas.setAlignment(Pos.CENTER_RIGHT);
        clas.setPromptText("ادخل اسم الصف");
        clas.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                stage.requestFocus();
            }else {
                message.setText("");
            }
        });
        stage.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        stage.setAlignment(Pos.CENTER_RIGHT);
        stage.setPromptText("ادخل المرحلة اساس ام ثانوى");
        stage.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                message.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل عدد الكتب من هذا الصنف");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                price.requestFocus();
            }else {
                message.setText("");
            }
        });
        price.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        price.setAlignment(Pos.CENTER_RIGHT);
        price.setPromptText("ادخل سعر الكتاب");
        //to make typing easy
        //press to enter to forward to next field
        price.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                submit.requestFocus();
                if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                        (!clas.getText().isEmpty()&&!clas.getText().trim().isEmpty()) &&
                        (!stage.getText().isEmpty()&&!stage.getText().trim().isEmpty()) &&
                        (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                        (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                    if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                        message.setText("ادخل رقما في حقلي العدد و السعر");
                    }else {
                        try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
                            Statement statement = con.createStatement();
                            statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT," +
                                    "class TEXT,stage TEXT,number INTEGER,price INTEGER,date TEXT)");
                            PreparedStatement ps = con.prepareStatement("INSERT INTO data(name,class,stage,number," +
                                    "price,date) VALUES(?,?,?,?,?,?)");
                            ps.setString(1,name.getText());
                            ps.setString(2,clas.getText());
                            ps.setString(3,stage.getText());
                            ps.setInt(4,Integer.parseInt(num.getText()));
                            ps.setInt(5,Integer.parseInt(price.getText()));
                            ps.setString(6, LocalDate.now().getYear()+"");
                            ps.execute();
                            name.clear();
                            clas.clear();
                            stage.clear();
                            num.clear();
                            price.clear();
                            message.setText("تم اضافة الكتاب بنجاح");
                            name.requestFocus();
                        }catch (SQLException e) {
                            System.out.println(e.getMessage());
                            message.setText("عفوا حدث خطأ ما");
                        }
                    }
                }else {
                    message.setText("لايمكنك ترك الحقول فارغا");
                }
            }else {
                message.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!clas.getText().isEmpty()&&!clas.getText().trim().isEmpty()) &&
                    (!stage.getText().isEmpty()&&!stage.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                    (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                    message.setText("ادخل رقما في حقلي العدد و السعر");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
                        Statement statement = con.createStatement();
                        statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT," +
                                "class TEXT,stage TEXT,number INTEGER,price INTEGER,date TEXT)");
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(name,class,stage,number," +
                                "price,date) VALUES(?,?,?,?,?,?)");
                        ps.setString(1,name.getText());
                        ps.setString(2,clas.getText());
                        ps.setString(3,stage.getText());
                        ps.setInt(4,Integer.parseInt(num.getText()));
                        ps.setInt(5,Integer.parseInt(price.getText()));
                        ps.setString(6, LocalDate.now().getYear()+"");
                        ps.execute();
                        name.clear();
                        clas.clear();
                        stage.clear();
                        num.clear();
                        price.clear();
                        message.setText("تم اضافة الكتاب بنجاح");
                        name.requestFocus();
                    }catch (SQLException e) {
                        System.out.println(e.getMessage());
                        message.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                message.setText("لايمكنك ترك الحقول فارغا");
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
        hClass.setSpacing(10.0);
        hClass.prefWidth(200.0);
        hClass.setAlignment(Pos.CENTER);
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
        getChildren().addAll(hName,hClass,hStage,hNum,hPrice,herror,hControle);
        setSpacing(15.0);
        setPrefWidth(500);
        setPrefHeight(450);
        setPadding(new Insets(40,0,0,0));
    }

    private void drawScreen(String bname) {
        //Labels
        Label message = new Label();
        Label lName = new Label("اسم الكتاب");
        Label lPrice = new Label("السعر");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField();
        TextField price = new TextField();
        TextField num = new TextField();

        //Buttons
        Button submit = new Button("اضافة");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(message);
        HBox hName = new HBox(name,lName);
        HBox hNum = new HBox(num,lNum);
        HBox hPrice = new HBox(price,lPrice);
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        message.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lPrice.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم الكتاب");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                message.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل عدد الكتب من هذا الصنف");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                price.requestFocus();
            }else {
                message.setText("");
            }
        });
        price.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        price.setAlignment(Pos.CENTER_RIGHT);
        price.setPromptText("ادخل سعر الكتاب");
        //to make typing easy
        //press to enter to forward to next field
        price.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                submit.requestFocus();
                if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                        (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                        (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                    if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                        message.setTextFill(Color.RED);
                        message.setText("ادخل رقما في حقلي العدد و السعر");
                    }else {
                        try(Connection con = DriverManager.getConnection("jdbc:sqlite:otherBooks.db")) {
                            Statement statement = con.createStatement();
                            statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT," +
                                    "number INTEGER,price INTEGER,date TEXT)");
                            PreparedStatement ps = con.prepareStatement("INSERT INTO data(name,number," +
                                    "price,date) VALUES(?,?,?,?)");
                            ps.setString(1,name.getText());
                            ps.setInt(2,Integer.parseInt(num.getText()));
                            ps.setInt(3,Integer.parseInt(price.getText()));
                            ps.setString(4, LocalDate.now().getYear()+"");
                            ps.execute();
                            name.clear();
                            num.clear();
                            price.clear();
                            message.setTextFill(Color.GREEN);
                            message.setText("تم اضافة الكتاب بنجاح");
                            name.requestFocus();
                        }catch (SQLException e) {
                            message.setTextFill(Color.RED);
                            message.setText("عفوا حدث خطأ ما");
                        }
                    }
                }else {
                    message.setTextFill(Color.RED);
                    message.setText("لايمكنك ترك الحقول فارغا");
                }
            }else {
                message.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                    (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                    message.setTextFill(Color.RED);
                    message.setText("ادخل رقما في حقلي العدد و السعر");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:otherBooks.db")) {
                        Statement statement = con.createStatement();
                        statement.execute("CREATE TABLE IF NOT EXISTS data(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT," +
                                "number INTEGER,price INTEGER,date TEXT)");
                        PreparedStatement ps = con.prepareStatement("INSERT INTO data(name,number," +
                                "price,date) VALUES(?,?,?,?)");
                        ps.setString(1,name.getText());
                        ps.setInt(2,Integer.parseInt(num.getText()));
                        ps.setInt(3,Integer.parseInt(price.getText()));
                        ps.setString(4, LocalDate.now().getYear()+"");
                        ps.execute();
                        name.clear();
                        num.clear();
                        price.clear();
                        message.setTextFill(Color.GREEN);
                        message.setText("تم اضافة الكتاب بنجاح");
                        name.requestFocus();
                    }catch (SQLException e) {
                        message.setTextFill(Color.RED);
                        message.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                message.setTextFill(Color.RED);
                message.setText("لايمكنك ترك الحقول فارغا");
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
        hNum.setSpacing(10.0);
        hNum.prefWidth(200.0);
        hNum.setAlignment(Pos.CENTER);
        hPrice.setSpacing(10.0);
        hPrice.setPrefWidth(200.0);
        hPrice.setAlignment(Pos.CENTER);
        hControle.setAlignment(Pos.CENTER);
        hControle.setSpacing(40.0);

        //Base Properties
        getChildren().addAll(hName,hNum,hPrice,herror,hControle);
        setSpacing(15.0);
        setPrefWidth(500);
        setPrefHeight(450);
        setPadding(new Insets(40,0,0,0));
    }
}

/////////////////////////////Edit Class/////////////////////////////////////////////////////
class Edit extends VBox {

    //////////////Constructor/////////////////////
    public Edit(int id,String tName,String tClass,String tStage,int tNum,int tPrice) {
        drawScreen(id,tName,tClass,tStage,tNum,tPrice);
    }
    /////////////////////////////////////////////

    //////////////Constructor/////////////////////
    public Edit(int id,String tName,int tNum,int tPrice) {
        drawScreen(id,tName,tNum,tPrice);
    }
    /////////////////////////////////////////////


    private void drawScreen(int id,String tName,String tClass,String tStage,int tNum,int tPrice) {
        //Labels
        Label message = new Label();
        Label lName = new Label("اسم الكتاب");
        Label lClass = new Label("الصف");
        Label lPrice = new Label("السعر");
        Label lStage = new Label ("المرحلة");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField(tName);
        TextField price = new TextField(tPrice+"");
        TextField clas = new TextField(tClass);
        TextField stage = new TextField(tStage);
        TextField num = new TextField(tNum+"");

        //Buttons
        Button submit = new Button("تعديل");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(message);
        HBox hName = new HBox(name,lName);
        HBox hClass = new HBox(clas,lClass);
        HBox hStage = new HBox(stage,lStage);
        HBox hNum = new HBox(num,lNum);
        HBox hPrice = new HBox(price,lPrice);
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        message.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lClass.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lStage.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lPrice.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم الكتاب");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                clas.requestFocus();
            }else {
                message.setText("");
            }
        });
        clas.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        clas.setAlignment(Pos.CENTER_RIGHT);
        clas.setPromptText("ادخل اسم الصف");
        clas.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                stage.requestFocus();
            }else {
                message.setText("");
            }
        });
        stage.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        stage.setAlignment(Pos.CENTER_RIGHT);
        stage.setPromptText("ادخل المرحلة اساس ام ثانوى");
        stage.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                message.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل عدد الكتب من هذا الصنف");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                price.requestFocus();
            }else {
                message.setText("");
            }
        });
        price.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        price.setAlignment(Pos.CENTER_RIGHT);
        price.setPromptText("ادخل سعر الكتاب");
        price.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                submit.requestFocus();
            }else {
                message.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!clas.getText().isEmpty()&&!clas.getText().trim().isEmpty()) &&
                    (!stage.getText().isEmpty()&&!stage.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                    (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                    message.setTextFill(Color.RED);
                    message.setText("ادخل رقما في حقلي العدد و السعر");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
                        PreparedStatement ps = con.prepareStatement("UPDATE data SET name=?,class=?,stage=?,number=?,price=?" +
                                "WHERE id=?");
                        ps.setString(1,name.getText());
                        ps.setString(2,clas.getText());
                        ps.setString(3,stage.getText());
                        ps.setInt(4,Integer.parseInt(num.getText()));
                        ps.setInt(5,Integer.parseInt(price.getText()));
                        ps.setInt(6,id);
                        ps.execute();
                        name.clear();
                        clas.clear();
                        stage.clear();
                        num.clear();
                        price.clear();
                        message.setTextFill(Color.GREEN);
                        message.setText("تم تعديل تفاصيل الكتاب بنجاح");
                    }catch (SQLException e) {
                        message.setTextFill(Color.RED);
                        message.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                message.setTextFill(Color.RED);
                message.setText("لايمكنك ترك الحقول فارغا");
            }
        });
        cancel.setOnAction(event -> new Books().edit_stage.close());

        //HBoxes's properties
        herror.setAlignment(Pos.CENTER);
        hName.setSpacing(10.0);
        hName.setPrefWidth(200.0);
        hName.setAlignment(Pos.CENTER);
        hClass.setSpacing(10.0);
        hClass.prefWidth(200.0);
        hClass.setAlignment(Pos.CENTER);
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
        getChildren().addAll(hName,hClass,hStage,hNum,hPrice,herror,hControle);
        setSpacing(15.0);
        setPrefWidth(500);
        setPrefHeight(450);
        setPadding(new Insets(40,0,0,0));
    }

    private void drawScreen(int id,String tName,int tNum,int tPrice) {
        //Labels
        Label message = new Label();
        Label lName = new Label("اسم الكتاب");
        Label lPrice = new Label("السعر");
        Label lNum = new Label ("العدد");

        //TextFields
        TextField name = new TextField(tName);
        TextField price = new TextField(tPrice+"");
        TextField num = new TextField(tNum+"");

        //Buttons
        Button submit = new Button("تعديل");
        Button cancel = new Button("الغاء");

        //HBoxes
        HBox herror = new HBox(message);
        HBox hName = new HBox(name,lName);
        HBox hNum = new HBox(num,lNum);
        HBox hPrice = new HBox(price,lPrice);
        HBox hControle = new HBox(cancel,submit);

        //Label Properties
        message.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lName.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lNum.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        lPrice.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));

        //TextField's Properties
        name.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        name.setAlignment(Pos.CENTER_RIGHT);
        name.setPromptText("ادخل اسم الكتاب");
        name.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                num.requestFocus();
            }else {
                message.setText("");
            }
        });
        num.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        num.setAlignment(Pos.CENTER_RIGHT);
        num.setPromptText("ادخل عدد الكتب من هذا الصنف");
        num.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                price.requestFocus();
            }else {
                message.setText("");
            }
        });
        price.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        price.setAlignment(Pos.CENTER_RIGHT);
        price.setPromptText("ادخل سعر الكتاب");
        price.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                submit.requestFocus();
            }else {
                message.setText("");
            }
        });

        //Button's properties
        submit.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        cancel.setFont(Font.font("DejaVu Serif",FontWeight.BOLD,24));
        submit.setOnAction(event -> {
            if((!name.getText().isEmpty()&&!name.getText().trim().isEmpty()) &&
                    (!num.getText().isEmpty()&&!num.getText().trim().isEmpty()) &&
                    (!price.getText().isEmpty()&&!price.getText().trim().isEmpty())) {
                if(!num.getText().matches("\\d*") || !price.getText().matches("\\d*")) {
                    message.setTextFill(Color.RED);
                    message.setText("ادخل رقما في حقلي العدد و السعر");
                }else {
                    try(Connection con = DriverManager.getConnection("jdbc:sqlite:otherBooks.db")) {
                        PreparedStatement ps = con.prepareStatement("UPDATE data SET name=?,number=?,price=?" +
                                "WHERE id=?");
                        ps.setString(1,name.getText());
                        ps.setInt(2,Integer.parseInt(num.getText()));
                        ps.setInt(3,Integer.parseInt(price.getText()));
                        ps.setInt(4,id);
                        ps.execute();
                        name.clear();
                        num.clear();
                        price.clear();
                        message.setTextFill(Color.GREEN);
                        message.setText("تم تعديل تفاصيل الكتاب بنجاح");
                    }catch (SQLException e) {
                        message.setTextFill(Color.RED);
                        message.setText("عفوا حدث خطأ ما");
                    }
                }
            }else {
                message.setTextFill(Color.RED);
                message.setText("لايمكنك ترك الحقول فارغا");
            }
        });
        cancel.setOnAction(event -> new Books().edit_stage.close());

        //HBoxes's properties
        herror.setAlignment(Pos.CENTER);
        hName.setSpacing(10.0);
        hName.setPrefWidth(200.0);
        hName.setAlignment(Pos.CENTER);
        hNum.setSpacing(10.0);
        hNum.prefWidth(200.0);
        hNum.setAlignment(Pos.CENTER);
        hPrice.setSpacing(10.0);
        hPrice.setPrefWidth(200.0);
        hPrice.setAlignment(Pos.CENTER);
        hControle.setAlignment(Pos.CENTER);
        hControle.setSpacing(40.0);

        //Base Properties
        getChildren().addAll(hName,hNum,hPrice,herror,hControle);
        setSpacing(15.0);
        setPrefWidth(500);
        setPrefHeight(450);
        setPadding(new Insets(40,0,0,0));
    }
}