package sample.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChartModel {

    private final StringProperty name;
    private final IntegerProperty num;

    public ChartModel(String name,int num) {
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleIntegerProperty(num);
    }

    public String getName() {
        return name.get();
    }

    public int getNum() {
        return num.get();
    }
}
