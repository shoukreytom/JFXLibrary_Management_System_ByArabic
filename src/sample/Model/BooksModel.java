package sample.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BooksModel {

    private final IntegerProperty id;
    private final StringProperty name;
    private StringProperty clas;
    private StringProperty stage;
    private final IntegerProperty num;
    private final IntegerProperty price;

    public BooksModel(Integer id,String name,String clas,String stage,Integer num,Integer price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.clas = new SimpleStringProperty(clas);
        this.stage = new SimpleStringProperty(stage);
        this.num = new SimpleIntegerProperty(num);
        this.price = new SimpleIntegerProperty(price);
    }

    public BooksModel(Integer id,String name,Integer num,Integer price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleIntegerProperty(num);
        this.price = new SimpleIntegerProperty(price);
    }

    public Integer getId(){
        return id.get();
    }
    public String getName(){
        return name.get();
    }
    public String getClas(){
        return clas.get();
    }
    public String getStage() {
        return stage.get();
    }
    public Integer getNum() {
        return num.get();
    }
    public Integer getPrice() {
        return price.get();
    }
}
