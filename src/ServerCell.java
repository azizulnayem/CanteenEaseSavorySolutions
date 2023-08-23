


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.Serializable;
import java.util.regex.Pattern;

public class ServerCell extends Label {

    public static DoubleProperty windowHeight = new SimpleDoubleProperty();
    public static DoubleProperty windowWidth = new SimpleDoubleProperty();

    public static boolean turn = true;
    private String value;

    public static ServerCell[][] cells = new ServerCell[3][3];

    public ServerCell(int row, int col) {

        super("");

        cells[row][col] = this;
        this.setValue("");
        this.prefHeightProperty().bind(windowHeight);
        this.prefWidthProperty().bind(windowWidth);
        this.setAlignment(Pos.CENTER);
        this.getStyleClass().add("cell");

        this.setOnMouseClicked(e -> {

            if (turn && this.getValue() == "") {
                this.setValue("X");
                this.setFont(Font.font(Font.getFamilies().get(17), FontWeight.BOLD, this.getPrefHeight() / 6));
                this.setText(this.getValue());
                this.setTextFill(Color.RED);
                turn = false;
                checkCells();

            } else if (this.getValue() == "") {
                this.setValue("O");

                this.setFont(Font.font(Font.getFamilies().get(17), FontWeight.BOLD, this.getPrefHeight() / 6));
                this.setText(this.getValue());
                this.setTextFill(Color.BLUE);
                turn = true;
                checkCells();
            }

        });
    }

    public static String checkCells() {

        Pattern xWon = Pattern.compile("(XXX......)|(X...X...X)|(..X.X.X..)|(.X..X..X.)|(X..X..X..)|(...XXX...)|(......XXX)|(..X..X..X)");
        Pattern oWon = Pattern.compile("(OOO......)|(O...O...O)|(..O.O.O..)|(.O..O..O.)|(O..O..O..)|(...OOO...)|(......OOO)|(..O..O..O)");
        String won = "";
        for (int i = 0; i < 9; i++) {
            if (cells[i / 3][i % 3].getValue() == null)
                won = won + "n";
            else
                won = won + cells[i / 3][i % 3].getValue();

        }
        System.out.println(won);
        if (won.matches(xWon.pattern())) {

            return "X";


        } else if (won.matches(oWon.pattern())) {



            return "O";
        }
        return "N";
    }

    private static Scene endScreen(String won) {

        Label endTitle = new Label(won + " won in Tic Tac Toe!");

        StackPane endScreen = new StackPane(endTitle);

        endTitle.setFont(Font.font(Font.getFamilies().get(17), FontWeight.BOLD, 40));
        endTitle.setTextFill(Color.GREEN);

        Scene scene = new Scene(endScreen, 300, 300);


        return scene;
    }
    public String getValue() {

        return this.value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {

            return true;
        }

        if (o instanceof ServerCell) {

            ServerCell other = (ServerCell)o;
            return this.getValue().equals(other.getValue());
        }

        return false;
    }
    @Override
    public String toString() {

        return String.format("%s", value);
    }

}