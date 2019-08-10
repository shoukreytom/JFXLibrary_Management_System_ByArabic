package sample.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ContentsModel {

    private final IntegerProperty id;
    private final StringProperty date;
    private final StringProperty name;
    private final IntegerProperty num;
    private final StringProperty mount;

    public ContentsModel(Integer id,String date,String name,Integer num,String mount) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleIntegerProperty(num);
        this.mount = new SimpleStringProperty(mount);
    }

    public String getName() {
        return name.get();
    }
    public int getNum() {
        return num.get();
    }
    public String getMount() {
        return mount.get();
    }
    public int getId() {
        return id.get();
    }
    public String getDate() {
        return date.get();
    }
}
