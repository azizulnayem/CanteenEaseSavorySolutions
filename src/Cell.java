

import javafx.scene.control.Label;

import java.io.Serializable;

public abstract class Cell extends Label implements Serializable {

    private String value;
    public Cell(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        return String.format("%s", value);
    }



}