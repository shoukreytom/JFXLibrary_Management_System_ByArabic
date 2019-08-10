package sample.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Main;
import sample.Model.ChartModel;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

public class Home {

    private ObservableList<String> list;
    @FXML
    public void contents() throws Exception{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/contents.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void books() throws Exception{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/books.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void logOut() throws Exception{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXML/user_login.fxml"));
        Parent root = loader.load();
        Main.stage.setScene(new Scene(root));
    }

    @FXML
    public void close() {
        Main.stage.close();
    }

    @FXML
    public void chart() {
        boolean show = false;
        ObservableList<XYChart.Data> bcData = FXCollections.observableArrayList();
        try(Connection con = DriverManager.getConnection("jdbc:sqlite:books.db")) {
            PreparedStatement ps = con.prepareStatement("SELECT name,number,date from data");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                bcData.addAll(new XYChart.Data(rs.getString("name"),rs.getInt("number")));
//                        series2.getData().add(new XYChart.Data(rs.getString("name"),rs.getInt("number")));
//                        series3.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                if(rs.getString("date").equalsIgnoreCase(year)) {
//                    series1.getData().add(new XYChart.Data(rs.getString("name"),rs.getInt("number")));
//                    series2.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                    series3.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                }else if(rs.getString("date").equalsIgnoreCase(year1)) {
//                    series2.getData().add(new XYChart.Data(rs.getString("name"),(rs.getInt("number"))));
//                    series1.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                    series3.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                }else if(rs.getString("date").equalsIgnoreCase(year2)) {
//                    series3.getData().add(new XYChart.Data(rs.getString("name"),(rs.getInt("number"))));
//                    series1.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                    series2.getData().add(new XYChart.Data(rs.getString("name"),0.0));
//                }
            }

        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        LocalDate date = LocalDate.now();
        String year = date.getYear()+"";
        String year1 = date.getYear()-1+"";
        String year2 = date.getYear()-2+"";
        System.out.println(year);
        Stage stage = new Stage();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("الكتب");
        yAxis.setLabel("العدد");
        xAxis.setStyle("-fx-font-size: 30px;-fx-font-family: 'DejaVu serif bold';");
        yAxis.setStyle("-fx-font-size: 30px;-fx-font-family: 'DejaVu serif bold';");
        xAxis.setTickLabelFont(Font.font("DejaVu serif", FontWeight.BOLD,18));
        yAxis.setTickLabelFont(Font.font("DejaVu serif", FontWeight.BOLD,18));
        xAxis.setOpacity(10.0);
        yAxis.setTickLength(5.0);
        BarChart bc = new BarChart(xAxis,yAxis);
        bc.setTitle("الكتب المدرسية");
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        XYChart.Series series3 = new XYChart.Series();
        series1.setName("العدد");
        series2.setName("السعر");
        series3.setName(year2);
        series1.getData().addAll(bcData);
        bc.getStylesheets().add("/sample/Style/style.css");

        ComboBox<Pair<String,String>> choices = new ComboBox<>();
        ComboBox<Pair<String,String>> book_types = new ComboBox<>();
        ComboBox<Pair<String,String>> book_class = new ComboBox<>();
        ComboBox<Pair<String,String>> book_stage = new ComboBox<>();
        List<Pair<String,String>> list = new ArrayList<>();
        list.add(new Pair<>("1","المحتويات"));
        list.add(new Pair<>("2","كتب مدرسية"));
        list.add(new Pair<>("3","كتب ثقافية"));
        List<Pair<String,String>> list2 = new ArrayList<>();
        list2.add(new Pair<>("1","ثانوي"));
        list2.add(new Pair<>("2","اساس"));
        List<Pair<String,String>> list3 = new ArrayList<>();
        list3.add(new Pair<>("1","الاول"));
        list3.add(new Pair<>("2","الثاني"));
        list3.add(new Pair<>("3","الثالث"));
        List<Pair<String,String>> list4 = new ArrayList<>();
        list4.add(new Pair<>("1","الاول"));
        list4.add(new Pair<>("2","الثاني"));
        list4.add(new Pair<>("3","الثالث"));
        list4.add(new Pair<>("3","الرابع"));
        list4.add(new Pair<>("3","الخامس"));
        list4.add(new Pair<>("3","السادس"));
        list4.add(new Pair<>("3","السابع"));
        list4.add(new Pair<>("3","الثامن"));
        Callback<ListView<Pair<String,String>>,ListCell<Pair<String,String>>> fac = (v) -> new ListCell<Pair<String,String>>() {

            @Override
            protected void updateItem(Pair<String,String> item,boolean isEmpty) {
                super.updateItem(item,isEmpty);
                if(isEmpty) {
                    setText("");

                }else {
                    setText(item.getValue());
                }
            }
        };
        choices.setStyle("-fx-font-family: 'dejaVu serif';-fx-font-size: 22px");
        book_types.setStyle("-fx-font-family: 'dejaVu serif';-fx-font-size: 22px");
        book_class.setStyle("-fx-font-family: 'dejaVu serif';-fx-font-size: 22px");
        book_stage.setStyle("-fx-font-family: 'dejaVu serif';-fx-font-size: 22px");
        choices.setOnAction(e -> {
            final String text = choices.getValue().getValue();
            if (text.equalsIgnoreCase("كتب مدرسية")) {
                book_class.setDisable(false);
                book_types.setDisable(false);
                book_stage.setDisable(false);
            } else if (text.equalsIgnoreCase("")) {
                book_class.setDisable(true);
                book_types.setDisable(true);
                book_stage.setDisable(true);
            } else {
                book_class.setDisable(true);
                book_types.setDisable(true);
                book_stage.setDisable(true);
            }
            System.out.println(text);
        });
        choices.setCellFactory(fac);
        choices.setButtonCell(fac.call(null));
        choices.getItems().addAll(list);

        book_class.setCellFactory(fac);
        book_class.setButtonCell(fac.call(null));
        book_class.getItems().addAll(list2);

        String v = "";
        book_class.setOnAction((event) -> {
            final String text = book_class.getValue().getValue();
            if(text.equalsIgnoreCase("ثانوي")) {
                book_stage.setCellFactory(fac);
                book_stage.setButtonCell(fac.call(null));
                book_stage.getItems().removeAll(list4);
                book_stage.getItems().addAll(list3);
            }else {
                book_stage.setCellFactory(fac);
                book_stage.setButtonCell(fac.call(null));
                book_stage.getItems().removeAll(list3);
                book_stage.getItems().addAll(list4);
            }
        });


        book_types.setDisable(true);
        book_class.setDisable(true);
        book_stage.setDisable(true);
        HBox hBox = new HBox(book_stage,book_class,choices);
        VBox vBox = new VBox(hBox,bc);
        vBox.setSpacing(10.0);
        hBox.setSpacing(10.0);
        GridPane.setVgrow(bc,Priority.ALWAYS);
        vBox.setPrefHeight(700);
        bc.setPrefHeight(700.0);
        hBox.setAlignment(Pos.CENTER);
        AnchorPane.setBottomAnchor(vBox,0.0);
        AnchorPane.setRightAnchor(vBox,0.0);
        AnchorPane.setLeftAnchor(vBox,0.0);
        AnchorPane.setTopAnchor(vBox,0.0);
        AnchorPane.setTopAnchor(hBox,0.0);

        AnchorPane.setBottomAnchor(bc,0.0);
        AnchorPane.setRightAnchor(bc,0.0);
        AnchorPane.setLeftAnchor(bc,0.0);

        stage.setScene(new Scene(new AnchorPane(vBox),1000,700));
        stage.initOwner(Main.stage);
        stage.show();
    }

    @FXML
    public void setting() {
        Button usr = new Button("اعدادات المستخدم");
        Button help = new Button("حول");
        Button display = new Button("المظهر");
        Button chusr = new Button("تغيير اسم المستخدم");
        Button chpas = new Button("نغيير كلمة المرور");
        ToggleGroup tg = new ToggleGroup();
        RadioButton dark = new RadioButton("ليلى");
        RadioButton light = new RadioButton("نهار");
        dark.setToggleGroup(tg);
        light.setToggleGroup(tg);
        Text text = new Text(AboutMe.about);
        text.setTextAlignment(TextAlignment.RIGHT);

        chusr.setStyle("-fx-background-color: transparent;");
        chpas.setStyle("-fx-background-color: transparent;");
        chusr.setTextFill(Color.BLUE);
        chpas.setTextFill(Color.BLUE);
        chusr.setFont(Font.font("dejaVu serif bold",18));
        chpas.setFont(Font.font("dejaVu serif bold",18));

        dark.setStyle("-fx-background-color: transparent;");
        dark.setTextFill(Color.BLUE);
        dark.setFont(Font.font("dejaVu serif bold",18));
        dark.setSelected(true);
        dark.setTextAlignment(TextAlignment.RIGHT);
        dark.setAlignment(Pos.CENTER_RIGHT);
        light.setStyle("-fx-background-color: transparent;");
        light.setTextFill(Color.BLUE);
        light.setFont(Font.font("dejaVu serif bold",18));

        VBox vBox = new VBox(display,usr,help);
        VBox vBox1 = new VBox();

        usr.setFont(Font.font("dejaVu serif bold",18));
        usr.setAlignment(Pos.CENTER_RIGHT);
        usr.setCursor(Cursor.HAND);
        usr.setTextFill(Color.BLUE);
        text.setFont(Font.font("dejaVu serif",FontWeight.BOLD,18));
        text.setStyle("-fx-text-color: white;");
        usr.setStyle("-fx-background-color: transparent;");
        usr.setOnAction(event -> {
            vBox1.getChildren().clear();
            vBox1.getChildren().addAll(chusr,chpas);
        });

        help.setOnAction(event -> {
            vBox1.getChildren().clear();
            vBox1.getChildren().addAll(text);
        });
        chusr.setOnAction(event -> {
            TextField textField = new TextField();
            vBox1.getChildren().add(textField);
        });
        display.setOnAction(event -> {
            vBox1.getChildren().clear();
            vBox1.getChildren().addAll(dark,light);
        });
        help.setTextFill(Color.BLUE);
        display.setTextFill(Color.BLUE);
        help.setStyle("-fx-background-color: transparent;");
        display.setStyle("-fx-background-color: transparent;");
        help.setCursor(Cursor.HAND);
        display.setCursor(Cursor.HAND);
        help.setFont(Font.font("dejaVu serif bold",18));
        display.setFont(Font.font("dejaVu serif bold",18));

        vBox.setSpacing(10.0);
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setPrefWidth(200.0);
        vBox.setStyle("-fx-background-color: rgb(22,22,22);");

        vBox1.setSpacing(10.0);
        vBox1.setAlignment(Pos.TOP_RIGHT);
        vBox1.setStyle("-fx-background-color: rgb(44,44,44);");

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(800.0,500.0);
        borderPane.setRight(vBox);
        borderPane.setCenter(vBox1);
        Stage stage = new Stage();
        stage.setScene(new Scene(borderPane));
        stage.initOwner(Main.stage);
        stage.show();
    }

    @FXML
    public void update() {

    }

    @FXML
    public void daysActivity() {
        FontAwesomeIconView addF = new FontAwesomeIconView();
        FontAwesomeIconView delF = new FontAwesomeIconView();
        FontAwesomeIconView editF = new FontAwesomeIconView();
        FontAwesomeIconView refF = new FontAwesomeIconView();
        addF.setGlyphSize(20.0);
        delF.setGlyphSize(20.0);
        editF.setGlyphSize(20.0);
        refF.setGlyphSize(20.0);
        addF.setFill(Color.GREEN);
        delF.setFill(Color.RED);
        editF.setFill(Color.PURPLE);
        refF.setFill(Color.BLUE);
        addF.setGlyphName("PLUS");
        editF.setGlyphName("EDIT");
        delF.setGlyphName("REMOVE");
        refF.setGlyphName("REFRESH");

        TreeItem<String> treeItem = new TreeItem<>("something");
        TreeItem<String> treeItem1 = new TreeItem<>("something else");
        treeItem.getChildren().add(treeItem1);
        TreeView<String> treeView = new TreeView<>(treeItem);

        Button add = new Button("اضافة");
        Button edit = new Button("تعديل");
        Button del = new Button("حذف");
        Button ref = new Button("تحديث");
        BorderPane pane = new BorderPane();
        VBox vBox = new VBox(add,edit,del,ref);
        VBox vBox1 = new VBox(treeView);
        vBox.setSpacing(10.0);
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setPadding(new Insets(5,0,0,0));
        vBox1.setAlignment(Pos.TOP_RIGHT);
        add.setAlignment(Pos.CENTER_RIGHT);
        edit.setAlignment(Pos.CENTER_RIGHT);
        del.setAlignment(Pos.CENTER_RIGHT);
        ref.setAlignment(Pos.CENTER_RIGHT);

        add.setContentDisplay(ContentDisplay.RIGHT);
        del.setContentDisplay(ContentDisplay.RIGHT);
        edit.setContentDisplay(ContentDisplay.RIGHT);
        ref.setContentDisplay(ContentDisplay.RIGHT);
        add.setGraphic(addF);
        del.setGraphic(delF);
        edit.setGraphic(editF);
        ref.setGraphic(refF);
        add.setStyle("-fx-background-color:transparent;-fx-font-family:'dejaVu serif bold';-fx-font-size:24px;-fx-text-fill:white");
        edit.setStyle("-fx-background-color:transparent;-fx-font-family:'dejaVu serif bold';-fx-font-size:24px;-fx-text-fill:white");
        del.setStyle("-fx-background-color:transparent;-fx-font-family:'dejaVu serif bold';-fx-font-size: 24px;-fx-text-fill:white");
        ref.setStyle("-fx-background-color:transparent;-fx-font-family:'dejaVu serif bold';-fx-font-size: 24px;-fx-text-fill:white");
        add.setOnAction(event -> {
            Label lDate = new Label("التاريخ");
            Label lName = new Label("اسم المحتوي");
            Label lBought = new Label("العدد المباع");
            Label lSold = new Label("العدد المشتري");

            lDate.setFont(Font.font("dejaVu serif bold",24));
            lName.setFont(Font.font("dejaVu serif bold",24));
            lBought.setFont(Font.font("dejaVu serif bold",24));
            lSold.setFont(Font.font("dejaVu serif bold",24));

            DatePicker date = new DatePicker();
            TextField name = new TextField();
            TextField bought = new TextField();
            TextField sold = new TextField();
            Button ok = new Button("اضافة");
            Button cancel = new Button("الغاء");
            ok.setFont(Font.font("dejaVu serif bold",24));
            cancel.setFont(Font.font("dejaVu serif bold",24));
            name.setFont(Font.font("dejaVu serif bold",24));
            bought.setFont(Font.font("dejaVu serif bold",24));
            sold.setFont(Font.font("dejaVu serif bold",24));

            HBox hBox = new HBox(date,lDate);
            HBox hBox1 = new HBox(name,lName);
            HBox hBox2 = new HBox(bought,lBought);
            HBox hBox3 = new HBox(sold,lSold);
            HBox hBox4 = new HBox(cancel,ok);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox1.setAlignment(Pos.CENTER_RIGHT);
            hBox2.setAlignment(Pos.CENTER_RIGHT);
            hBox3.setAlignment(Pos.CENTER_RIGHT);
            hBox4.setAlignment(Pos.CENTER);
            hBox.setSpacing(60.0);
            hBox1.setSpacing(10);
            hBox2.setSpacing(15);
            hBox3.setSpacing(15);
            hBox4.setSpacing(60.0);
            VBox con = new VBox(hBox,hBox1,hBox2,hBox3,hBox4);
            con.setAlignment(Pos.CENTER);
            con.setSpacing(10.0);
            con.setPadding(new Insets(5,5,5,5));

            ok.setOnAction(event1 -> {
                TreeItem<LocalDate> item = new TreeItem<>(date.getValue());
                vBox1.getChildren().add(new TreeView<>(item));
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(con,500,300));
            stage.show();
        });

        edit.setOnAction(event -> {
            Label lDate = new Label("التاريخ");
            Label lName = new Label("اسم المحتوي");
            Label lBought = new Label("العدد المباع");
            Label lSold = new Label("العدد المشتري");

            lDate.setFont(Font.font("dejaVu serif bold",24));
            lName.setFont(Font.font("dejaVu serif bold",24));
            lBought.setFont(Font.font("dejaVu serif bold",24));
            lSold.setFont(Font.font("dejaVu serif bold",24));

            DatePicker date = new DatePicker();
            TextField name = new TextField();
            TextField bought = new TextField();
            TextField sold = new TextField();

            Button ok = new Button("تعديل");
            Button cancel = new Button("الغاء");
            ok.setFont(Font.font("dejaVu serif bold",24));
            cancel.setFont(Font.font("dejaVu serif bold",24));

            HBox hBox = new HBox(date,lDate);
            HBox hBox1 = new HBox(name,lName);
            HBox hBox2 = new HBox(bought,lBought);
            HBox hBox3 = new HBox(sold,lSold);
            HBox hBox4 = new HBox(cancel,ok);
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox1.setAlignment(Pos.CENTER_RIGHT);
            hBox2.setAlignment(Pos.CENTER_RIGHT);
            hBox3.setAlignment(Pos.CENTER_RIGHT);
            hBox4.setAlignment(Pos.CENTER);
            hBox.setSpacing(60.0);
            hBox1.setSpacing(10);
            hBox2.setSpacing(60.0);
            hBox3.setSpacing(60.0);
            hBox4.setSpacing(60.0);
            VBox con = new VBox(hBox,hBox1,hBox2,hBox3,hBox4);
            ok.setOnAction(event1 -> {
                treeItem.setValue(date.getValue()!=null?date.getValue().toString():"");
            });

            con.setAlignment(Pos.CENTER);
            con.setSpacing(10.0);
            Stage stage = new Stage();
            stage.setScene(new Scene(con));
            stage.show();
        });

        vBox.setStyle("-fx-background-color: #222222");
        vBox1.setStyle("-fx-background-color: #444444");
        vBox.setPrefWidth(200);

        pane.setRight(vBox);
        pane.setCenter(vBox1);
        Stage stage = new Stage();
        stage.setScene(new Scene(pane,700,500));
        stage.show();
    }
}

class AboutMe {
    static String about = "هذا البرنامج عبارة عن احد سلسلة برامج الادارة الذكية تمكنك من اداراة\n مكتبك بكفاءة وتوفر" +
            "البرنامج ادوات فعالة لكي تسهل لك الامور\n في التعامل مع محتويات مكتبك ايضا يعطيك النسبة المئوية للمحتويات\n" +
            "حتي تعلم ماهي الصنف الاكثر طلبا لدي زبائنك وذلك كله من دون اي ذرة جهد منك \n وذلك هو هدفنا ان نسهل " +
            "ونوفر لكل من بالادارة وقته وجهده\n وان شاء الله ترقبوا معنا في الاصدار القادم حتي تكونوا علي علم بمنتجاتنا" +
            "الجديدة\n\n" +
            "Developer: Shourkey Tom";
}